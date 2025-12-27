package com.joker.apostas.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${app.jwt.secret}")
    private final String secretKey;

    @Value("${app.jwt.expiration-seconds:3600}")
    private final long expirationSeconds;

    // Constructor to initialize final fields, without lombok @RequiredArgsConstructor because of
    // secret handling
    public JwtServiceImpl(
            @Value("${app.jwt.secret}") String secretKey,
            @Value("${app.jwt.expiration-seconds:3600}") long expirationSeconds) {

        // Transform the secret IMMEDIATELY.
        // The raw 'secretKey' argument exists only during this constructor call.
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.expirationSeconds = expirationSeconds;
    }

    /** Creates a JWT token for the given username */
    public String createToken(String username) {
        log.debug("Generating JWT for user: {}", username);
        try {
            Date now = new Date();
            Date validity =
                    new Date(
                            now.getTime()
                                    + (expirationSeconds
                                            * 1000)); // TODO validate that this multiplication is

            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            String token =
                    JWT.create()
                            .withSubject(username)
                            .withIssuedAt(now)
                            .withExpiresAt(validity)
                            .sign(algorithm);

            log.trace("JWT successfully created for {}", username);
            return token;
        } catch (Exception e) {
            log.error("CRITICAL: Failed to sign JWT for user {}: {}", username, e.getMessage());
            throw new RuntimeException("Failed to create JWT token", e);
        }
    }

    /** Extracts the username from a JWT token */
    public String extractUsername(String token) {
        try {
            return decodeToken(token).getSubject();
        } catch (TokenExpiredException e) {
            log.warn("JWT validation failed: Token has expired. Details: {}", e.getMessage());
            return null;
        } catch (SignatureVerificationException e) {
            log.error("SECURITY ALERT: JWT signature mismatch! Possible tampering attempt.");
            return null;
        } catch (JWTVerificationException e) {
            log.warn("JWT validation failed: Invalid token format or claims.");
            return null;
        }
    }

    /** Validates the token against the UserDetails */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        boolean isValid =
                username != null
                        && username.equals(userDetails.getUsername())
                        && !isTokenExpired(token);

        if (!isValid) {
            log.debug("Token validation result: false for user: {}", userDetails.getUsername());
        }
        return isValid;
    }

    /** Checks if the token is expired */
    private boolean isTokenExpired(String token) {
        try {
            Date expiresAt = decodeToken(token).getExpiresAt();
            boolean expired = expiresAt.before(new Date());
            if (expired) {
                log.debug("Token expired at: {}", expiresAt);
            }
            return expired;
        } catch (JWTVerificationException e) {
            return true;
        }
    }

    /** Decodes and verifies the token */
    private DecodedJWT decodeToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}

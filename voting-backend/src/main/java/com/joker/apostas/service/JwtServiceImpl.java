package com.joker.apostas.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
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

    // Constructor to initialize final fields, without lombok @RequiredArgsConstructor because of secret handling
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
        try {
            Date now = new Date();
            Date validity =
                    new Date(
                            now.getTime()
                                    + (expirationSeconds
                                            * 1000)); // TODO validate that this multiplication is
            // correct

            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withSubject(username)
                    .withIssuedAt(now)
                    .withExpiresAt(validity)
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("Error creating JWT token: {}", e.getMessage());
            throw new RuntimeException("Failed to create JWT token", e);
        }
    }

    /** Extracts the username from a JWT token */
    public String extractUsername(String token) {
        try {
            return decodeToken(token).getSubject();
        } catch (JWTVerificationException e) {
            log.warn("Invalid token: {}", e.getMessage());
            return null;
        }
    }

    /** Validates the token against the UserDetails */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username != null
                && username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    /** Checks if the token is expired */
    private boolean isTokenExpired(String token) {
        try {
            return decodeToken(token).getExpiresAt().before(new Date());
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

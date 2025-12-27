package com.joker.apostas.config;

import com.joker.apostas.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 1. Check if the header is missing or doesn't start with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.trace(
                    "No Bearer token found in request headers for path: {}",
                    request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract the token (after "Bearer ")
        jwt = authHeader.substring(7);
        log.debug("JWT detected, attempting extraction of username...");

        try {
            username = jwtService.extractUsername(jwt);
            log.debug("Username '{}' extracted. Loading details from database...", username);

            // 3. If username exists and user isn't already authenticated in this context
            if (username != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                log.debug("Token is valid. Setting security context for user: '{}'", username);

                // 4. Validate token against the database user
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    // 5. Update the Security Context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    log.warn("Security check failed: Token is invalid for user '{}'", username);
                }
            }
        } catch (Exception e) {
            log.error("Authentication filter error: {}", e.getMessage());
        }

        // 6. Always continue the filter chain
        filterChain.doFilter(request, response);
    }
}

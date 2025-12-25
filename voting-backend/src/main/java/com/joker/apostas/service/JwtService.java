package com.joker.apostas.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String createToken(String username);

    String extractUsername(String token);

    boolean isTokenValid(String token, UserDetails userDetails);
}

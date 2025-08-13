package com.joker.apostas.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joker.apostas.service.AuthService;
import com.joker.apostas.auth.RegisterRequest;
import com.joker.apostas.auth.TokenResponse;
import com.joker.apostas.auth.LoginRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    // Constructor injection instead of @RequiredArgsConstructor
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public TokenResponse register(@RequestBody RegisterRequest request) {
        log.info("Register endpoint called with username={}", request.getUsername());
        return authService.register(request);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        log.info("Login endpoint called with username={}", request.getUsername());
        return authService.login(request);
    }
}

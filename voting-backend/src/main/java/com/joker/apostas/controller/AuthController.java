package com.joker.apostas.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;

import java.util.Map;

import com.joker.apostas.service.AuthService;
import com.joker.apostas.auth.RegisterRequest;
import com.joker.apostas.auth.TokenResponse;
import com.joker.apostas.auth.LoginRequest;

import jakarta.servlet.http.HttpServletResponse;

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

    private void setAuthCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(false)       // TODO: change this in production
                .sameSite("None")
                .path("/")
                .maxAge(7200)       // 2 hours
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request,  HttpServletResponse response) {
        log.info("Register endpoint called with username={}", request.getUsername());
        TokenResponse tokenResponse = authService.register(request);

        setAuthCookie(response, tokenResponse.getToken());
        return ResponseEntity.ok(Map.of("message", "registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request,  HttpServletResponse response) {
        log.info("Login endpoint called with identifier={}", request.getIdentifier());
        
        TokenResponse tokenResponse = authService.login(request);

        setAuthCookie(response, tokenResponse.getToken());
        return ResponseEntity.ok(Map.of("message", "logged in"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // clear cookie
        ResponseCookie cookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(false)       // TODO: change this in production
                .sameSite("None")
                .path("/")
                .maxAge(0)          // expire immediately
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(Map.of("message", "logged out"));
    }

}

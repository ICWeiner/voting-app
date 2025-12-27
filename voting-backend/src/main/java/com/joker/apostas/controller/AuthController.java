package com.joker.apostas.controller;

import com.joker.apostas.dto.LoginDto;
import com.joker.apostas.dto.SignUpDto;
import com.joker.apostas.dto.UserDto;
import com.joker.apostas.service.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid LoginDto loginDto) {
        log.info("REST request to login user: '{}'", loginDto.getIdentifier());
        UserDto userDto = authService.login(loginDto);

        log.info("User '{}' logged in successfully", userDto.getUsername());
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto signUpDto) {
        log.info(
                "REST request to register new user: '{}' ({})",
                signUpDto.getUsername(),
                signUpDto.getEmail());
        UserDto createdUser = authService.register(signUpDto);

        log.info(
                "User '{}' registered successfully with ID: {}",
                createdUser.getUsername(),
                createdUser.getId());
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId()))
                .body(createdUser);
    }
}

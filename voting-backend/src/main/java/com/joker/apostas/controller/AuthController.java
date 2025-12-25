package com.joker.apostas.controller;

import com.joker.apostas.dto.LoginDto;
import com.joker.apostas.dto.SignUpDto;
import com.joker.apostas.dto.UserDto;
import com.joker.apostas.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid LoginDto loginDto) {
        UserDto userDto = authService.login(loginDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto signUpDto) {
        UserDto createdUser = authService.register(signUpDto);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId()))
                .body(createdUser);
    }
}

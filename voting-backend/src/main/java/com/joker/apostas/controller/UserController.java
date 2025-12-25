package com.joker.apostas.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/me")//TODO: this method is only here for test purposes, will probably go at some point
    public ResponseEntity<Map<String, Object>> getMyProfile() {
        // Get the authentication object from the Security Context (set by our Filter)
        var auth = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> profile = new HashMap<>();
        profile.put("username", auth.getName());
        profile.put("authorities", auth.getAuthorities());
        profile.put("isAuthenticated", auth.isAuthenticated());

        return ResponseEntity.ok(profile);
    }
}

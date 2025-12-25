package com.joker.apostas.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {
    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl("testSecretKeyForJwtTesting1234567890", 3600L);
    }

    @Test
    void createToken_ShouldGenerateValidString() {
        String token = jwtService.createToken("joker");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_ShouldReturnCorrectName() {
        String token = jwtService.createToken("joker");
        String username = jwtService.extractUsername(token);
        assertEquals("joker", username);
    }

    @Test
    void isTokenValid_ShouldReturnTrueForCorrectUser() {
        String username = "joker";
        String token = jwtService.createToken(username);

        // Mocking UserDetails behavior
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        username, "pass", java.util.Collections.emptyList());

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }
}

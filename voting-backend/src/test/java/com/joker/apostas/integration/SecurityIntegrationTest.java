package com.joker.apostas.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.joker.apostas.dto.SignUpDto;
import com.joker.apostas.dto.UserDto;
import com.joker.apostas.service.AuthService;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@Transactional
public class SecurityIntegrationTest extends AbstractIntegrationTest {
    @Autowired private MockMvc mockMvc;

    @Autowired private AuthService authService;

    @Test
    void shouldAccessMeEndpoint_WhenTokenIsValid() throws Exception {
        // 1. Register a user to get a valid token
        SignUpDto signUpDto =
                new SignUpDto().username("tester").email("tester@mail.com").password("password123");
        UserDto userDto = authService.register(signUpDto);
        String token = userDto.getToken();

        // 2. Try to access the protected /me endpoint
        mockMvc.perform(get("/api/users/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tester"))
                .andExpect(jsonPath("$.isAuthenticated").value(true));
    }

    @Test
    void shouldReturnForbidden_WhenTokenIsMissing() throws Exception {
        mockMvc.perform(get("/api/users/me")).andExpect(status().isForbidden());
        // Note: Depending on your config, this might be 403 or 401
    }
}

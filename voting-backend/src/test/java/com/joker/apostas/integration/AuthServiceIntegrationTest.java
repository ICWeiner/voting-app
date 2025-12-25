package com.joker.apostas.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.joker.apostas.dto.LoginDto;
import com.joker.apostas.dto.SignUpDto;
import com.joker.apostas.dto.UserDto;
import com.joker.apostas.exception.AppException;
import com.joker.apostas.model.User;
import com.joker.apostas.model.enums.Role;
import com.joker.apostas.repository.UserRepository;
import com.joker.apostas.service.AuthService;

import com.joker.apostas.service.JwtService;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@Transactional
class AuthServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired private AuthService authService;

    @Autowired private JwtService jwtService;

    @Autowired private UserRepository userRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void register_ShouldPersistUserAndReturnValidToken() {
        // Arrange
        SignUpDto signUpDto =
                new SignUpDto()
                        .username("joker_integration")
                        .email("integration@mail.com")
                        .password("securePass123");

        // Act
        UserDto result = authService.register(signUpDto);

        // Assert
        assertNotNull(result.getToken(), "Token should be generated upon registration");
        assertEquals("joker_integration", result.getUsername());

        // JWT Validation: Ensure the token actually belongs to this user
        String extractedUser = jwtService.extractUsername(result.getToken());
        assertEquals("joker_integration", extractedUser);

        // Database Verification
        User savedUser = userRepository.findByUsername("joker_integration").orElseThrow();
        assertTrue(passwordEncoder.matches("securePass123", savedUser.getPassword()));
    }

    @Test
    void login_ShouldSucceedAndReturnValidToken() {
        // Arrange
        String username = "login_test";
        User user = new User();
        user.setUsername(username);
        user.setEmail("login@test.com");
        user.setPassword(passwordEncoder.encode("correct_password"));
        user.setRole(Role.USER);
        userRepository.save(user);

        LoginDto loginDto = new LoginDto().identifier(username).password("correct_password");

        // Act
        UserDto result = authService.login(loginDto);

        // Assert
        assertNotNull(result.getToken(), "Token should be generated upon login");

        // JWT Validation: Verify the token claims
        String extractedUser = jwtService.extractUsername(result.getToken());
        assertEquals(username, extractedUser);

        // Optional: Verify the token is actually valid according to the service
        assertTrue(jwtService.isTokenValid(result.getToken(), user));
    }

    @Test
    void login_ShouldThrowUnauthorized_WhenPasswordIsIncorrect() {
        // Arrange
        User user = new User();
        user.setUsername("wrong_pass_user");
        user.setEmail("wrong@pass.com");
        user.setPassword(passwordEncoder.encode("secret"));
        user.setRole(Role.USER);
        userRepository.save(user);

        LoginDto loginDto = new LoginDto().identifier("wrong_pass_user").password("wrong_attempt");

        // Act & Assert
        AppException ex = assertThrows(AppException.class, () -> authService.login(loginDto));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        assertEquals("Invalid credentials", ex.getMessage());
    }
}

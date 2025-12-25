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

    @Autowired private UserRepository userRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void register_ShouldPersistUserInLxcPostgres() {
        // Arrange
        SignUpDto signUpDto =
                new SignUpDto()
                        .username("joker_integration")
                        .email("integration@mail.com")
                        .password("securePass123");

        // Act
        UserDto result = authService.register(signUpDto);

        // Assert
        assertNotNull(result);
        assertEquals("joker_integration", result.getUsername());

        // Verify it's actually in the database
        User savedUser = userRepository.findByUsername("joker_integration").orElseThrow();
        assertTrue(passwordEncoder.matches("securePass123", savedUser.getPassword()));
        assertEquals(Role.USER, savedUser.getRole());
    }

    @Test
    void login_ShouldSucceed_WithRealHashedPassword() {
        // Arrange
        User user = new User();
        user.setUsername("login_test");
        user.setEmail("login@test.com");
        user.setPassword(passwordEncoder.encode("correct_password"));
        user.setRole(Role.USER);
        userRepository.save(user);

        LoginDto loginDto = new LoginDto().identifier("login_test").password("correct_password");

        // Act
        UserDto result = authService.login(loginDto);

        // Assert
        assertNotNull(result);
        assertEquals("login_test", result.getUsername());
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

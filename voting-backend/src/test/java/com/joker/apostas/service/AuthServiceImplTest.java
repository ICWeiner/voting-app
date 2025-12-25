package com.joker.apostas.service;

import com.joker.apostas.dto.SignUpDto;
import com.joker.apostas.exception.AppException;
import com.joker.apostas.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Test
    void register_ShouldThrowException_WhenUserExists() {
        // Arrange
        SignUpDto dto = new SignUpDto().username("joker").email("test@mail.com");
        when(userRepository.existsByUsername("joker")).thenReturn(true);

        // Act & Assert
        assertThrows(AppException.class, () -> authServiceImpl.register(dto));
    }

    @Test
    void register_ShouldCallRepositoryWithLowercasedUsername() {
        // Arrange
        SignUpDto dto = new SignUpDto().username("MiXeD_CaSe").email("test@mail.com").password("pass");

        // We want to verify that the service checks for the LOWERCASE version
        when(userRepository.existsByUsername("mixed_case")).thenReturn(true);

        // Act & Assert
        assertThrows(AppException.class, () -> authServiceImpl.register(dto));

        // Verify the mock was called with the normalized string
        verify(userRepository).existsByUsername("mixed_case");
    }
}


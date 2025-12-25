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
}


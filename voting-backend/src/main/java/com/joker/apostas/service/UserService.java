package com.joker.apostas.service;

import com.joker.apostas.dto.LoginDto;
import com.joker.apostas.dto.SignUpDto;
import com.joker.apostas.dto.UserDto;
import com.joker.apostas.exception.AppException;
import com.joker.apostas.mapper.UserMapper;
import com.joker.apostas.model.User;
import com.joker.apostas.model.enums.UserType;
import com.joker.apostas.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    @Autowired private UserRepository userRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private UserMapper userMapper;

    public UserDto login(LoginDto loginDto) {
        String identifier = loginDto.getIdentifier(); // either username or email

        // Try finding by username first, then by email
        User user =
                userRepository
                        .findByUsername(identifier)
                        .or(() -> userRepository.findByEmail(identifier))
                        .orElseThrow(
                                () -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(
                CharBuffer.wrap(loginDto.getPassword()), user.getPassword())) {
            throw new AppException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        return userMapper.toUserDto(user);
    }

    public UserDto register(SignUpDto signUpDto) {
        // Check if username already exists
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new AppException("Username already exists", HttpStatus.BAD_REQUEST);
        }

        // Check if email already exists
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new AppException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(signUpDto);
        user.setRole(UserType.USER); // Default role for new users
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(signUpDto.getPassword())));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}

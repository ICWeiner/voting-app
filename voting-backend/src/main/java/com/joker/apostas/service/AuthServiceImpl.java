package com.joker.apostas.service;

import com.joker.apostas.dto.LoginDto;
import com.joker.apostas.dto.SignUpDto;
import com.joker.apostas.dto.UserDto;
import com.joker.apostas.exception.AppException;
import com.joker.apostas.mapper.UserMapper;
import com.joker.apostas.model.User;
import com.joker.apostas.model.enums.Role;
import com.joker.apostas.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired private UserRepository userRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private UserMapper userMapper;

    @Autowired private JwtService jwtService;

    public UserDto login(LoginDto loginDto) {
        String identifier = loginDto.getIdentifier(); // either username or email

        // Try finding by username and email
        User user =
                userRepository
                        .findByUsernameOrEmail(identifier, identifier)
                        .orElseThrow(
                                () ->
                                        new AppException(
                                                "Invalid credentials", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new AppException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        UserDto userDto = userMapper.toUserDto(user);
        // Generate the token here
        userDto.setToken(jwtService.createToken(user.getUsername()));

        return userDto;
    }

    @Transactional
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
        user.setRole(Role.USER); // Default role for new users
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        User savedUser = userRepository.save(user);

        UserDto createdUser = userMapper.toUserDto(savedUser);
        createdUser.setToken(jwtService.createToken(signUpDto.getUsername()));

        return createdUser;
    }
}

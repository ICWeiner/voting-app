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

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired private UserRepository userRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private UserMapper userMapper;

    @Autowired private JwtService jwtService;

    private static final Set<String> RESERVED_WORDS =
            Set.of("admin", "support", "root", "system", "administrator", "null", "undefined");

    public UserDto login(LoginDto loginDto) {
        // 1. Normalize identifier
        String identifier = loginDto.getIdentifier().toLowerCase().trim();
        ; // either username or email

        // 2. Find user by username or email
        User user =
                userRepository
                        .findByUsernameOrEmail(identifier, identifier)
                        .orElseThrow(
                                () ->
                                        new AppException(
                                                "Invalid credentials", HttpStatus.UNAUTHORIZED));

        // 3. Verify password
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new AppException("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        // 4. Generate token and return UserDto
        UserDto userDto = userMapper.toUserDto(user);
        userDto.setToken(jwtService.createToken(user.getUsername()));

        return userDto;
    }

    @Transactional
    public UserDto register(SignUpDto signUpDto) {
        // 1. Normalize username and email
        signUpDto.setUsername(signUpDto.getUsername().toLowerCase().trim());
        signUpDto.setEmail(signUpDto.getEmail().toLowerCase().trim());

        // 2. Validate against reserved words
        if (RESERVED_WORDS.contains(signUpDto.getUsername())) {
            throw new AppException(
                    "Username '" + signUpDto.getUsername() + "' is not allowed",
                    HttpStatus.BAD_REQUEST);
        }
        String emailPrefix = signUpDto.getEmail().split("@")[0];
        if (RESERVED_WORDS.contains(emailPrefix)) {
            throw new AppException(
                    "Email prefix '" + emailPrefix + "' is not allowed", HttpStatus.BAD_REQUEST);
        }

        // 3. Check for existing username or email
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new AppException("Username already exists", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new AppException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        // 4. Create and save new user
        User user = userMapper.signUpToUser(signUpDto);
        user.setRole(Role.USER); // Default role for new users
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        User savedUser = userRepository.save(user);

        UserDto createdUser = userMapper.toUserDto(savedUser);
        createdUser.setToken(jwtService.createToken(signUpDto.getUsername()));

        return createdUser;
    }
}

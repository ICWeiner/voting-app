package com.joker.apostas.service;

import com.joker.apostas.dto.LoginDto;
import com.joker.apostas.dto.SignUpDto;
import com.joker.apostas.dto.UserDto;
import com.joker.apostas.exception.AppException;
import com.joker.apostas.mapper.UserMapper;
import com.joker.apostas.model.User;
import com.joker.apostas.model.enums.Role;
import com.joker.apostas.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final JwtService jwtService;

    private static final Set<String> RESERVED_WORDS =
            Set.of("admin", "support", "root", "system", "administrator", "null", "undefined");

    public UserDto login(LoginDto loginDto) {
        // 1. Normalize identifier
        String identifier = loginDto.getIdentifier().toLowerCase().trim();
        log.debug("Attempting login for identifier: {}", identifier);
        ; // either username or email

        // 2. Find user by username or email
        User user =
                userRepository
                        .findByUsernameOrEmail(identifier, identifier)
                        .orElseThrow(
                                () -> {
                                    log.warn(
                                            "Login failed: User not found for identifier '{}'",
                                            identifier);
                                    return new AppException(
                                            "Invalid credentials", HttpStatus.UNAUTHORIZED);
                                });

        // 3. Verify password
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            log.warn("Login failed: Password mismatch for user '{}'", user.getUsername());
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

        log.debug("Processing registration for username: {}", signUpDto.getUsername());

        // 2. Validate against reserved words
        if (RESERVED_WORDS.contains(signUpDto.getUsername())) {
            log.warn(
                    "Registration rejected: Username '{}' is a reserved word",
                    signUpDto.getUsername());
            throw new AppException(
                    "Username '" + signUpDto.getUsername() + "' is not allowed",
                    HttpStatus.BAD_REQUEST);
        }
        String emailPrefix = signUpDto.getEmail().split("@")[0];
        if (RESERVED_WORDS.contains(emailPrefix)) {
            log.warn("Registration rejected: Email prefix '{}' is a reserved word", emailPrefix);
            throw new AppException(
                    "Email prefix '" + emailPrefix + "' is not allowed", HttpStatus.BAD_REQUEST);
        }

        // 3. Check for existing username or email
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            log.warn(
                    "Registration rejected: Username '{}' already exists", signUpDto.getUsername());
            throw new AppException("Username already exists", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            log.warn("Registration rejected: Email '{}' already exists", signUpDto.getEmail());
            throw new AppException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        // 4. Create and save new user
        log.info("Creating new account for user '{}'", signUpDto.getUsername());

        signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword())); // hash password

        User user = userMapper.signUpToUser(signUpDto);
        user.setRole(Role.USER); // Default role for new users

        User savedUser = userRepository.save(user);

        log.debug("User saved to database. ID: {}", savedUser.getId());

        UserDto createdUser = userMapper.toUserDto(savedUser);
        createdUser.setToken(jwtService.createToken(signUpDto.getUsername()));

        return createdUser;
    }
}

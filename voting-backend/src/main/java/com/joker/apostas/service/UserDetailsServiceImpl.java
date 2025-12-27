package com.joker.apostas.service;

import com.joker.apostas.model.User;
import com.joker.apostas.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user details for username: '{}'", username);

        return userRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> {
                            // This is a WARN because it means a valid JWT was presented,
                            // but the user no longer exists in our DB (e.g., deleted account).
                            log.warn(
                                    "Security Error: User '{}' not found in database during token validation",
                                    username);
                            return new UsernameNotFoundException("User not found: " + username);
                        });
    }
}

package com.joker.apostas.security;

import com.joker.apostas.model.User;
import com.joker.apostas.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired private UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user =
                repo.findByEmail(identifier)
                        .or(() -> repo.findByUsername(identifier))
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Convert UserType enum to authority string with "ROLE_" prefix
        String role = "ROLE_" + user.getRole().toString();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role)));
    }
}

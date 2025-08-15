package com.joker.apostas.security;

import com.joker.apostas.model.User;
import com.joker.apostas.repository.UserRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository repo;

  public CustomUserDetailsService(UserRepository repo) {
    this.repo = repo;
  }

  @Override
  public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
    User user = repo.findByEmail(identifier)
        .or(() -> repo.findByUsername(identifier))
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return new org.springframework.security.core.userdetails.User(
            user.getUsername(), 
            user.getPasswordHash(), 
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
            );
  }
}

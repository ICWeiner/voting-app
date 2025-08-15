package com.joker.apostas.security;

import com.joker.apostas.model.User;
import com.joker.apostas.repository.UserRepository;
import java.util.Arrays;
import java.util.Collection;
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
    User ua = repo.findByEmail(identifier)
        .or(() -> repo.findByUsername(identifier))
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    Collection<GrantedAuthority> auths =
        Arrays.stream(ua.getRole().split(","))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
              .collect(Collectors.toList());

    return new org.springframework.security.core.userdetails.User(
        ua.getUsername(), ua.getPasswordHash(), auths);
  }
}

package com.joker.apostas.security;

import com.joker.apostas.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);


  public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain)
      throws ServletException, IOException {
    
    String header = request.getHeader("Authorization");
    if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      log.info("JWT token: {}", token);
      try {
        String username = jwtService.getUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          
          UserDetails user = userDetailsService.loadUserByUsername(username);

          UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (Exception e) {
          log.warn("JWT authentication failed: {}", e.getMessage());
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
          return;
      }
    }
    filterChain.doFilter(request, response);
  }
}

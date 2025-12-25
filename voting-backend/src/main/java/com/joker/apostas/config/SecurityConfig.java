package com.joker.apostas.config;

import com.joker.apostas.security.CustomUserAuthenticationProvider;
import com.joker.apostas.service.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    private final CustomUserAuthenticationProvider customUserAuthenticationProvider;

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtService, userDetailsService);

        http.exceptionHandling(
                        exceptionHandling ->
                                exceptionHandling.authenticationEntryPoint(
                                        userAuthenticationEntryPoint))

                // Add JWT filter before the BasicAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)

                // CSRF disabled for stateless REST APIs
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Define which routes are public vs secured
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(HttpMethod.POST, "/auth/**", "/public/**")
                                        .permitAll()
                                        .requestMatchers("/vote/**")
                                        .hasAnyRole("USER", "ADMIN")
                                        // .requestMatchers("/contests/**").hasAnyRole("USER",
                                        // "ADMIN")
                                        .anyRequest()
                                        .authenticated())
                .authenticationProvider(
                        customUserAuthenticationProvider) // ✅ use for username/password login
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

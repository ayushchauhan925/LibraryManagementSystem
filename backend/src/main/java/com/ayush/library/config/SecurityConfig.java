package com.ayush.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())          // disable CSRF for APIs
            .cors(cors -> {})                      // enable CORS using WebConfig
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/student/**").permitAll() // login/logout
                .requestMatchers("/students/me").authenticated() // must be logged in
                .anyRequest().permitAll()
            )
            // ✅ Enable session-based authentication
            .formLogin(form -> form.disable()) // we don’t want Spring’s default login form
            .httpBasic(basic -> basic.disable()) // disable HTTP Basic
            .sessionManagement(session -> session
                .maximumSessions(1)
            );

        return http.build();
    }
}

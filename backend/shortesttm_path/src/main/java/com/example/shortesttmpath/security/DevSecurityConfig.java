package com.example.shortesttmpath.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The Spring Security configuration class for the dev profile used in development.
 */
@Configuration
@Profile("dev")
public class DevSecurityConfig {
    /**
     * The default constructor.
     */
    public DevSecurityConfig() {
    }

    /**
     * Disables Spring Security in dev.
     * @param http The HttpSecurity.
     * @return The SecurityFilterChain.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}

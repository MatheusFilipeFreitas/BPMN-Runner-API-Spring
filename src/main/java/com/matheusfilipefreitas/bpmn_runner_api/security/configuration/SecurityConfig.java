package com.matheusfilipefreitas.bpmn_runner_api.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.matheusfilipefreitas.bpmn_runner_api.security.filter.FirebaseCorsFilter;

import jakarta.servlet.Filter;

@Configuration
public class SecurityConfig {
    
@Bean
    public SecurityFilterChain filterChain(HttpSecurity http, FirebaseCorsFilter firebaseCorsFilter) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/actuator/health").permitAll()

                    .anyRequest().permitAll()
                )
                .addFilterAfter((Filter) firebaseCorsFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}

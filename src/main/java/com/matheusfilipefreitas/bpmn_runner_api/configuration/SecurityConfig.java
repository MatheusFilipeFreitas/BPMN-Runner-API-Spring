package com.matheusfilipefreitas.bpmn_runner_api.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.matheusfilipefreitas.bpmn_runner_api.security.DynamicCorsConfigurationSource;
import com.matheusfilipefreitas.bpmn_runner_api.security.FirebaseTokenFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private FirebaseTokenFilter firebaseTokenFilter;

    // Injetaremos isso no Passo 3
    @Autowired
    private DynamicCorsConfigurationSource dynamicCorsConfigurationSource; 

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Configurar CORS Dinâmico (ver Passo 3)
            .cors(cors -> cors.configurationSource(dynamicCorsConfigurationSource))
            
            // 2. Desabilitar CSRF (comum para APIs stateless baseadas em token)
            .csrf(csrf -> csrf.disable())

            // 3. Tornar a sessão stateless (não criar sessões HTTP)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 4. Configurar regras de autorização
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/public/**", "/actuator/health").permitAll()
                .anyRequest().authenticated()
            )

            // 5. Adicionar nosso filtro Firebase antes do filtro padrão de username/password
            .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
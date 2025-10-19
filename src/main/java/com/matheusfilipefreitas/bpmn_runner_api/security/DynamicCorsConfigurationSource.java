package com.matheusfilipefreitas.bpmn_runner_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import com.matheusfilipefreitas.bpmn_runner_api.security.AllowedOrigins;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class DynamicCorsConfigurationSource implements CorsConfigurationSource {

    @Autowired
    private AllowedOrigins allowedOriginsService;

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        String originHeader = request.getHeader("Origin");
        List<String> allowedOrigins = allowedOriginsService.getAllowedOrigins();

        CorsConfiguration config = new CorsConfiguration();
        
        // Verifica se a origem da requisição está na nossa lista do Firestore
        if (allowedOrigins.contains(originHeader)) {
            config.addAllowedOrigin(originHeader); // Permite a origem específica
        }
        // Se a origem não estiver na lista, NENHUMA origem será adicionada
        // e o navegador bloqueará a requisição (falha no CORS).

        config.addAllowedHeader("*"); // Permite todos os cabeçalhos (Authorization, Content-Type, etc)
        config.addAllowedMethod("*"); // Permite todos os métodos (GET, POST, PUT, DELETE, OPTIONS)
        config.setAllowCredentials(true); // Permite credenciais (necessário para cookies/auth)

        return config;
    }
}
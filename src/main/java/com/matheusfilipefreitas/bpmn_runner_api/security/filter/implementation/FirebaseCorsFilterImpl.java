package com.matheusfilipefreitas.bpmn_runner_api.security.filter.implementation;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.matheusfilipefreitas.bpmn_runner_api.security.filter.FirebaseCorsFilter;
import com.matheusfilipefreitas.bpmn_runner_api.security.service.FirebaseCorsService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FirebaseCorsFilterImpl implements FirebaseCorsFilter, Filter {
    private static final List<String> TRUSTED_CLIENT_ORIGINS = List.of(
        "https://ambitious-island-060dfc40f.1.azurestaticapps.net",
        "https://bpmn-runner.dev"
    );
    private final FirebaseCorsService firebaseCorsService;

    public FirebaseCorsFilterImpl(FirebaseCorsService firebaseCorsService) {
        this.firebaseCorsService = firebaseCorsService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        if (path.equals("/api/actuator/health")) {
            chain.doFilter(req, res);
            return;
        }

        String origin = request.getHeader("Origin");
        String apiKey = request.getHeader("X-Api-Key");

        if (origin != null) {
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                applyCorsHeaders(response, origin);
            }
            if (isTrustedClient(origin)) {
                applyCorsHeaders(response, origin);
            }
            else if (apiKey != null && apiKey.length() > 0 && firebaseCorsService.isOriginAllowed(origin, apiKey)) {
                applyCorsHeaders(response, origin);
            }
        }

        String requestMethod = request.getMethod();
        if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        if (origin == null) {
            if (apiKey != null && apiKey.length() > 0) {
                if (!firebaseCorsService.isApiKeyAllowed(apiKey)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Invalid or expired API key, go to website and renew it");
                    return;
                }
                applyCorsHeaders(response, origin);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("No valid Api-Key found, please pass header: 'X-Api-Key'");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    private boolean isTrustedClient(String origin) {
        return TRUSTED_CLIENT_ORIGINS.stream()
                .anyMatch(o -> o.equalsIgnoreCase(origin));
    }

    private void applyCorsHeaders(HttpServletResponse response, String origin) {
        if (origin != null) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }
        response.setHeader("Vary", "Origin");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Api-Key");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}

package com.matheusfilipefreitas.bpmn_runner_api.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheusfilipefreitas.bpmn_runner_api.security.AllowedOrigins;

@RestController
@RequestMapping("/admin/cache")
@Profile("!test")
@CrossOrigin(origins = "*") 
public class CorsController {

    @Autowired
    private AllowedOrigins allowedOriginsService;

    // @PreAuthorize("isAuthenticated()") 
    @PostMapping("/refresh-cors")
    public ResponseEntity<String> refreshCorsCache() {
        allowedOriginsService.refreshAllowedOriginsCache();
        return ResponseEntity.ok("Cache de CORS atualizado.");
    }
}

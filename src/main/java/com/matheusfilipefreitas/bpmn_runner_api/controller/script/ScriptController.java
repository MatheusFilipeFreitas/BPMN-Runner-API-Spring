package com.matheusfilipefreitas.bpmn_runner_api.controller.script;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheusfilipefreitas.bpmn_runner_api.service.script.ScriptService;

import lombok.AllArgsConstructor;

@AllArgsConstructor

@RestController
@RequestMapping("script")
public class ScriptController {
    private final ScriptService service; 

    @PostMapping(value = "/execute", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> executeScript(@AuthenticationPrincipal String uid, @RequestBody String code) {
        String result = service.processScript(code);
        return ResponseEntity.ok(result);
    }
}

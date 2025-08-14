package com.matheusfilipefreitas.bpmn_runner_api.controller.script;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor

@RestController
@RequestMapping("script")
public class ScriptController {

    @GetMapping("/execute")
    public String executeScript() {
        return "Excute script endpoint called";
    }

    @GetMapping("/validate")
    public String validateScript() {
        return "Validate script endpoint called";
    }
}

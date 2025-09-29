package com.matheusfilipefreitas.bpmn_runner_api.controller.script;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheusfilipefreitas.bpmn_runner_api.service.script.ScriptService;

import lombok.AllArgsConstructor;

@AllArgsConstructor

@RestController
@RequestMapping("script")
public class ScriptController {
    private final ScriptService service; 

    @PostMapping("/execute")
    public String executeScript() {
            String script = """
                pool(cliente, "Cliente") {
                    process(procCliente, "Fluxo do Cliente") { 
                        start(s1); 
                        task(t1, "Preencher pedido", USER); 
                        task(t2, "Enviar pedido", AUTOMATED) -> message(t3);
                        end(e1); 
                    } 
                } 
                pool(loja, "Loja Online") { 
                    process(procLoja, "Fluxo da Loja") { 
                        start(s2); 
                        task(t3, "Receber pedido", MANUAL); 
                        gateway(g1, "Verificar estoque", EXCLUSIVE) { 
                            yes -> {
                                task(t4, "Enviar nota fiscal", USER);
                            }
                            no -> task(t5, "Informar indisponibilidade", AUTOMATED) -> message(t1); 
                        }; 
                        end(e2); 
                    } 
                }
                """;
            service.processScript(script);
            return "Excute script endpoint called";
    }
}

package com.matheusfilipefreitas.bpmn_runner_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class BpmnRunnerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BpmnRunnerApiApplication.class, args);
	}

}

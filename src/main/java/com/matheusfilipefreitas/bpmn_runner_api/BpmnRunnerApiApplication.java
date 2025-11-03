package com.matheusfilipefreitas.bpmn_runner_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BpmnRunnerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BpmnRunnerApiApplication.class, args);
	}

}

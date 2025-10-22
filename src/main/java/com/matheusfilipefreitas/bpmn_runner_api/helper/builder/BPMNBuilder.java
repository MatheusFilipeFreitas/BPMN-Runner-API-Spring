package com.matheusfilipefreitas.bpmn_runner_api.helper.builder;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;

public interface BPMNBuilder {
    BpmnModelInstance buildModel();
}

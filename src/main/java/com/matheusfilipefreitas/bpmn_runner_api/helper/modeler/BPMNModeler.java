package com.matheusfilipefreitas.bpmn_runner_api.helper.modeler;

import java.util.List;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;

public interface BPMNModeler {
    void modelEntitiesIntoModel(BpmnModelInstance model, List<CommonBPMNIdEntity> entities);
    void modelConnectionsIntoModel(BpmnModelInstance model, List<ConnectionBPMNEntity> connections);
}

package com.matheusfilipefreitas.bpmn_runner_api.helper.builder.implementation;

import java.util.Collection;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnDiagram;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnEdge;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnPlane;
import org.camunda.bpm.model.bpmn.instance.bpmndi.BpmnShape;
import org.camunda.bpm.model.bpmn.instance.dc.Bounds;
import org.springframework.stereotype.Component;

import com.matheusfilipefreitas.bpmn_runner_api.helper.builder.BPMNBuilder;

@Component
public class BPMNBuilderImpl implements BPMNBuilder {
    private final String NAMESPACE = "http://camunda.org/examples";

    @Override
    public BpmnModelInstance buildModel() {
        BpmnModelInstance modelInstance = Bpmn.createEmptyModel();
        setDefinitionsIntoModel(modelInstance);
        return modelInstance;
    }

    private void setDefinitionsIntoModel(BpmnModelInstance modelInstance) {
        Definitions modelDefinitions = modelInstance.newInstance(Definitions.class);
        modelDefinitions.setTargetNamespace(NAMESPACE);
        modelInstance.setDefinitions(modelDefinitions);
    }
}

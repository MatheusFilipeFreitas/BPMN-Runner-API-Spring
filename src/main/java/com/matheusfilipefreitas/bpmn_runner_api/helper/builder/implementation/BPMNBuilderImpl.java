package com.matheusfilipefreitas.bpmn_runner_api.helper.builder.implementation;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Definitions;
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

        org.camunda.bpm.model.bpmn.instance.Process process =
                modelInstance.newInstance(org.camunda.bpm.model.bpmn.instance.Process.class);
        process.setId("mainProcess");
        modelDefinitions.addChildElement(process);

        modelInstance.setDefinitions(modelDefinitions);
    }
}

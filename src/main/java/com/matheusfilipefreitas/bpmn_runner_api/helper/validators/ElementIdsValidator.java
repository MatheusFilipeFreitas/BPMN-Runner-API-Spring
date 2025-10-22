package com.matheusfilipefreitas.bpmn_runner_api.helper.validators;

import java.util.List;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementInfo;

public interface ElementIdsValidator {
    void validate(List<ElementInfo> rawElements);
    void validateAllElementsInConnections(List<String> elementsIds, List<ConnectionBPMNEntity> connections);
}

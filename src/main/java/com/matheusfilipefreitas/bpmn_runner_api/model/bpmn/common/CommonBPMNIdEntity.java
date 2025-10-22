package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor

@Getter
@Setter
public abstract class CommonBPMNIdEntity {
    protected String id;
    protected String processId;
    protected Integer index;
}

package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class CommonBPMNIdEntity {
    protected String id;
    protected String label;
}

package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CommonBPMNIdAndLabelEntity extends CommonBPMNIdEntity {
    protected String label;

    public CommonBPMNIdAndLabelEntity(String id, String label, String processId) {
        super(id, processId);
        this.label = label;
    }
}

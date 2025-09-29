package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.types.EventType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Event extends CommonBPMNIdEntity {
    protected EventType type;
    
    protected Event(String id, EventType type, String processId) {
        super(id, processId);
        this.type = type;
    }
}

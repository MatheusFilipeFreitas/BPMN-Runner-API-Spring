package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.types.EventType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartEvent extends Event {
    public StartEvent(String id, String processId) {
        super(id, EventType.START, processId);
    }

        @Override
    public String toString() {
        return "StartEvent{" +
                "id='" + id + '\'' +
                (type != null ? ", type='" + type + '\'' : "") +
                (processId != null ? ", processId='" + processId + '\'' : "") +
                '}';
    }
}

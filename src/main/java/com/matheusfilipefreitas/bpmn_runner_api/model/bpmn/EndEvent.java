package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.types.EventType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EndEvent extends Event {
    public EndEvent(String id, String processId, Integer index) {
        super(id, EventType.END, processId, index);
    }

    @Override
    public String toString() {
        return "EndEvent{" +
                "id='" + id + '\'' +
                (type != null ? ", type='" + type + '\'' : "") +
                (processId != null ? ", processId='" + processId + '\'' : "") +
                '}';
    }
}

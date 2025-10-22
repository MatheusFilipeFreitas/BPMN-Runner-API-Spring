package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdAndLabelEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.types.TaskType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task extends CommonBPMNIdAndLabelEntity {
    private TaskType type;

    public Task(String id, String label, String type, String processId, Integer index) {
        super(id, label, processId, index);
        this.type = TaskType.fromString(type);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id='" + id + '\'' +
                (label != null ? ", label='" + label + '\'' : "") +
                (type != null ? ", type='" + type + '\'' : "") +
                (processId != null ? ", processId='" + processId + '\'' : "") +
                '}';
    }
}

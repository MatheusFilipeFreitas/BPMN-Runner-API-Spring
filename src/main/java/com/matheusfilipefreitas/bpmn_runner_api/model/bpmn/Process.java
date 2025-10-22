package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdAndLabelEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Process extends CommonBPMNIdAndLabelEntity {
    public Process(String id, String label, Integer index) {
        super(id, label, id, index);
    }

    @Override
    public String toString() {
        return "Process{" +
                "id='" + id + '\'' +
                (label != null ? ", label='" + label + '\'' : "") +
                (processId != null ? ", processId='" + processId + '\'' : "") +
                '}';
    }
}

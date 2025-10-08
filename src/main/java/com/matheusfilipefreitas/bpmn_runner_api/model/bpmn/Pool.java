package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdAndLabelEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pool extends CommonBPMNIdAndLabelEntity {
    public Pool(String id, String label, String processId, Integer index) {
        super(id, label, processId, index);
    }

    @Override
    public String toString() {
        return "Pool{" +
                "id='" + id + '\'' +
                (label != null ? ", label='" + label + '\'' : "") +
                (processId != null ? ", processId='" + processId + '\'' : "") +
                '}';
    }
}

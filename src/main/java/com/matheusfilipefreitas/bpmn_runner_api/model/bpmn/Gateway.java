package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn;

import java.util.ArrayList;
import java.util.List;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdAndLabelEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.container.GatewayContainer;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.types.GatewayType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Gateway extends CommonBPMNIdAndLabelEntity {
    private GatewayType type;
    private List<GatewayContainer> childrens = new ArrayList<>();

    public Gateway(String id, String label, String type, String processId, Integer index) {
        super(id, label, processId, index);
        this.type = GatewayType.fromString(type);
    }

    public Gateway(String id, String label, GatewayType type, List<GatewayContainer> childrens, String processId, Integer index) {
        super(id, label, processId, index);
        this.type = type;
        this.childrens = childrens;
    }

    @Override
    public String toString() {
        return "Gateway{" +
                "id='" + id + '\'' +
                (label != null ? ", label='" + label + '\'' : "") +
                (type != null ? ", type='" + type + '\'' : "") +
                (processId != null ? ", processId='" + processId + '\'' : "") +
                '}';
    }
}

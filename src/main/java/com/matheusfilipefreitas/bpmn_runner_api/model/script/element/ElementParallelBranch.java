package com.matheusfilipefreitas.bpmn_runner_api.model.script.element;

import java.util.LinkedHashMap;
import java.util.List;

import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.BranchType;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.ElementType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElementParallelBranch extends ElementBranch {
    private final List<LinkedHashMap<String, ElementType>> childrenIdsMap;

    public ElementParallelBranch(String gatewayId, List<LinkedHashMap<String, ElementType>> childrenIdsMap) {
        super(gatewayId, BranchType.PARALLEL);
        this.childrenIdsMap = childrenIdsMap;
    }
}

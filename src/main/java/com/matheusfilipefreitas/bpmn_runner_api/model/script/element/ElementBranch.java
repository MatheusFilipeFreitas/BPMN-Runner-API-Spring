package com.matheusfilipefreitas.bpmn_runner_api.model.script.element;

import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.BranchType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor

@Getter
public abstract class ElementBranch {
    protected String gatewayId;
    protected BranchType type;
}

package com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.container;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
public class GatewayContainer {
    private String startLine;
    private String endLine;
    private String label;
    private List<String> composeIds;
}

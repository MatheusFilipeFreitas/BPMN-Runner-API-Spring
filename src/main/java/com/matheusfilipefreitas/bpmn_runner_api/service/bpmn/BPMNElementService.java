package com.matheusfilipefreitas.bpmn_runner_api.service.bpmn;

import java.util.List;

import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementInfo;
import com.matheusfilipefreitas.bpmn_runner_api.service.common.genericcrudservice.CommonCrudService;

public interface BPMNElementService extends CommonCrudService<CommonBPMNIdEntity, String> {
    void saveEntitiesFromElementInfoList(List<ElementInfo> elementsInfo);
}

package com.matheusfilipefreitas.bpmn_runner_api.service.bpmn.implementation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.matheusfilipefreitas.bpmn_runner_api.common.exception.notfound.NotFoundException;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.EndEvent;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.Gateway;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.Pool;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.Process;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.StartEvent;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.Task;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementInfo;
import com.matheusfilipefreitas.bpmn_runner_api.repository.bpmn.BPMNEntitiesRepository;
import com.matheusfilipefreitas.bpmn_runner_api.service.bpmn.BPMNElementService;

import lombok.AllArgsConstructor;

@AllArgsConstructor

@Service
public class BPMNElementServiceImpl implements BPMNElementService {
    private final BPMNEntitiesRepository repository;

    @Override
    public List<CommonBPMNIdEntity> findAll() {
        return repository.getAllEntities();
    }

    @Override
    public CommonBPMNIdEntity findById(String id) {
        Optional<CommonBPMNIdEntity> entity = repository.getEntityById(id);
        if (entity.isEmpty()) {
            throw new NotFoundException("Could not find an entity with id: " + id);
        }
        return entity.get();
    }

    @Override
    public void save(CommonBPMNIdEntity entity) {
        repository.addEntity(entity);
    }

    @Override
    public void saveEntitiesFromElementInfoList(List<ElementInfo> elementsInfo) {
        for (int i = 0; i < elementsInfo.size(); i++) {
            Optional<CommonBPMNIdEntity> entity = this.getEntityFromElementInfo(elementsInfo.get(i), i + 1);
            if (!entity.isEmpty()) {
                this.save(entity.get());
            }
        }
    }

    @Override
    public void clearEntities() {
        repository.resetEntities();
    }

    private Optional<CommonBPMNIdEntity> getEntityFromElementInfo(ElementInfo elementInfo, int index) {
        switch(elementInfo.getElementType()) {
            case END_EVENT -> {
                return Optional.ofNullable(
                    new EndEvent(elementInfo.getId(), elementInfo.getProcessId(), index)
                );
            }
            case GATEWAY -> {
                return Optional.ofNullable(
                    new Gateway(elementInfo.getId(), elementInfo.getLabel(), elementInfo.getType(), elementInfo.getProcessId(), index)
                );
            }
            case POOL -> {
                return Optional.ofNullable(
                     new Pool(elementInfo.getId(), elementInfo.getLabel(), elementInfo.getProcessId(), index)
                );
            }
            case PROCESS -> {
                return Optional.ofNullable(
                    new Process(elementInfo.getId(), elementInfo.getLabel(), index)
                );
            }
            case START_EVENT -> {
                return Optional.ofNullable(
                    new StartEvent(elementInfo.getId(), elementInfo.getProcessId(), index)
                );
            }
            case TASK -> {
                return Optional.ofNullable(
                    new Task(elementInfo.getId(), elementInfo.getLabel(), elementInfo.getType(), elementInfo.getProcessId(), index)
                );
            }
            default -> {
                return Optional.ofNullable(null);
            }
        }
    }
}

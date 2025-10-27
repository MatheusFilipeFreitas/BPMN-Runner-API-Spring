package com.matheusfilipefreitas.bpmn_runner_api.service.bpmn.implementation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.matheusfilipefreitas.bpmn_runner_api.common.exception.notfound.NotFoundException;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.Gateway;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.common.CommonBPMNIdEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.connection.ConnectionBPMNEntity;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.types.ConnectionType;
import com.matheusfilipefreitas.bpmn_runner_api.model.bpmn.types.GatewayType;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementBranch;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementExclusiveBranch;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementInfo;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.ElementParallelBranch;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.BranchOrder;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.BranchType;
import com.matheusfilipefreitas.bpmn_runner_api.model.script.element.types.ElementType;
import com.matheusfilipefreitas.bpmn_runner_api.repository.bpmn.BPMNEntitiesRepository;
import com.matheusfilipefreitas.bpmn_runner_api.service.bpmn.BPMNConnectionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor

@Service
public class BPMNConnectionServiceImpl implements BPMNConnectionService {
    private final BPMNEntitiesRepository repository;

    @Override
    public List<ConnectionBPMNEntity> findAll() {
        return this.repository.getAllConnections();
    }

    @Override
    public ConnectionBPMNEntity findById(String id) {
        Optional<ConnectionBPMNEntity> connection = this.repository.getConnectionByEntityId(id);
        if (connection.isEmpty()) {
            throw new NotFoundException("Could not find connection entity with id: " + id);
        }
        return connection.get();
    }

    @Override
    public void save(ConnectionBPMNEntity entity) {
        repository.addConnection(entity);
    }

    @Override
    public void saveConnectionsFromElementInfo(List<ElementInfo> elementsInfo, List<ElementBranch> branchesInfo) {
        List<ConnectionBPMNEntity> connections = getConnectionEntityFromElementInfoList(elementsInfo, branchesInfo);
        for (ConnectionBPMNEntity connection : connections) {
            throwIfEntitiesWereNotCreated(connection.getConnectionEntityIdsAsList());
            save(connection);
        }
    }

    @Override
    public void resetConnections() {
        repository.resetConnections();
    }

    private List<ConnectionBPMNEntity> getConnectionEntityFromElementInfoList(List<ElementInfo> elementsInfo, List<ElementBranch> branchesInfo) {
        var orderedElementsInfo = elementsInfo.stream()
            .sorted(Comparator.comparing(ElementInfo::getIndex))
            .filter(element -> element.getElementType() != ElementType.POOL &&
                element.getElementType() != ElementType.PROCESS).toList();

        return buildConnectionsFromFlow(orderedElementsInfo, branchesInfo);
    }

    private List<ConnectionBPMNEntity> buildConnectionsFromFlow(
                                        List<ElementInfo> elements,
                                        List<ElementBranch> branches) {
        List<ConnectionBPMNEntity> connections = new ArrayList<>();
        int indexDefinition = 0;
        ElementBranch currentBranch = null;

        for (int i = 0; i < elements.size() - 1; i++) {
            ElementInfo current = elements.get(i);
            ElementInfo next = elements.get(i + 1);

            List<ConnectionBPMNEntity> duplicatedConnection = connections.stream().filter(c -> c.getSourceId().equals(current.getId()) && c.getTargetId().equals(next.getId())).toList();
            if (!duplicatedConnection.isEmpty()) {
                continue;
            }

            if (current.getElementType() == ElementType.GATEWAY) {
                var branchOpt = findBranchForGateway(current.getId(), branches);
                
                if (branchOpt.isPresent()) {
                    currentBranch = branchOpt.get();
                    handleConnectionsByBranchType(connections, branchOpt.get(), indexDefinition, current);
                    connectBranchsToNextElement(branchOpt.get(), elements, connections, indexDefinition, current.getProcessId());
                    continue;
                }
            }

            if (next.getElementType() == ElementType.MESSAGE) {
                String messageTargetId = next.getId();

                if (messageTargetId != null && !messageTargetId.isBlank()) {
                    connections.add(new ConnectionBPMNEntity(
                        current.getId(), messageTargetId, ConnectionType.MESSAGE, null, indexDefinition++
                    ));
                }

                int index = IntStream.range(0, elements.size())
                    .filter(e -> elements.get(e).getId().equals(next.getId()) && elements.get(e).getElementType() != ElementType.MESSAGE)
                    .findFirst()
                    .orElse(-1);

                i = index - 1;
                continue;
            }

            if (!next.getProcessId().equals(current.getProcessId())) {
                continue;
            }

            if (currentBranch != null && !isElementInsideABranch(current.getId(), currentBranch)) {
                currentBranch = null;
            }

            if (currentBranch != null && areIdsFromDifferentBranches(current.getId(), next.getId(), currentBranch)) {
                continue;
            }

            if (current.getIndex() > next.getIndex() || current.getId().equals(next.getId())) {
                continue;
            }

            connections.add(new ConnectionBPMNEntity(
                current.getId(), next.getId(), ConnectionType.SEQUENCE, null, indexDefinition++
            ));
        }

        return connections;
    }

    private boolean isElementInsideABranch(String currentId, ElementBranch branch) {
        if (branch.getType() == BranchType.EXCLUSIVE) {
            ElementExclusiveBranch exclusiveBranch = (ElementExclusiveBranch) branch;
            BranchOrder currentBranch = exclusiveBranch.getIdInsideBranches(currentId);
            return currentBranch != null;
        }
        if (branch.getType() == BranchType.PARALLEL) {
            ElementParallelBranch parallelBranch = (ElementParallelBranch) branch;
            LinkedHashMap<String, ElementType> currentScope = parallelBranch.getScopeFromElementId(currentId);
            return currentScope != null;
        }
        return false;
    }

    private void connectBranchsToNextElement(ElementBranch branch, List<ElementInfo> elements, List<ConnectionBPMNEntity> connections, int indexDefinition, String processId) {
        List<ElementInfo> filteredElements = elements.stream().filter(e -> e.getElementType() != ElementType.MESSAGE && e.getElementType() != ElementType.POOL && e.getElementType() != ElementType.PROCESS).toList();
        if (branch.getType() == BranchType.EXCLUSIVE) {
            ElementExclusiveBranch exclusiveBranch = (ElementExclusiveBranch) branch;
            String lastGatewayElementId = exclusiveBranch.getLastElementFromBranchs();

            int index = IntStream.range(0, filteredElements.size())
                .filter(i -> filteredElements.get(i).getId().equals(lastGatewayElementId))
                .findFirst()
                .orElse(-1);

            if (index == -1) {
                throw new RuntimeException("Could not find any element to create last elements connection");
            }

            if (index == filteredElements.size() - 1) {
                return;
            }

            ElementInfo nextLastElement = filteredElements.get(index + 1);
            repository.addGatewayClosingEntity(new Gateway(exclusiveBranch.getGatewayId() + "_end", null, branch.getType().toString(), processId, index + 1), nextLastElement.getId());

            if (!exclusiveBranch.hasYesBranchMessageElements() && !exclusiveBranch.hasNoBranchMessageElements() && !exclusiveBranch.hasYesBranchEndElements() && !exclusiveBranch.hasNoBranchEndElements()) {
                connections.add(
                    new ConnectionBPMNEntity(exclusiveBranch.getLastYesElement(), exclusiveBranch.getGatewayId() + "_end", ConnectionType.SEQUENCE, null, indexDefinition++)
                );
                connections.add(
                    new ConnectionBPMNEntity(exclusiveBranch.getLastNoElement(), exclusiveBranch.getGatewayId() + "_end", ConnectionType.SEQUENCE, null, indexDefinition++)
                );
                connections.add(
                    new ConnectionBPMNEntity(exclusiveBranch.getGatewayId() + "_end", nextLastElement.getId(), ConnectionType.SEQUENCE, null, indexDefinition++)
                );
            } else {
                if (!exclusiveBranch.hasYesBranchMessageElements() && !exclusiveBranch.hasYesBranchEndElements()) {
                    connections.add(
                        new ConnectionBPMNEntity(exclusiveBranch.getLastYesElement(), nextLastElement.getId(), ConnectionType.SEQUENCE, null, indexDefinition++)
                    );
                }

                if (!exclusiveBranch.hasNoBranchMessageElements() && !exclusiveBranch.hasNoBranchEndElements()) {
                    connections.add(
                        new ConnectionBPMNEntity(exclusiveBranch.getLastNoElement(), nextLastElement.getId(), ConnectionType.SEQUENCE, null, indexDefinition++)
                    );
                }
            }
        }

        if (branch.getType() == BranchType.PARALLEL) {
            ElementParallelBranch parallelBranch = (ElementParallelBranch) branch;
            String lastGatewayElementId = parallelBranch.findLastElementInBranch();

            int index = IntStream.range(0, filteredElements.size())
                .filter(i -> filteredElements.get(i).getId().equals(lastGatewayElementId))
                .findFirst()
                .orElse(-1);

            if (index == -1) {
                throw new RuntimeException("Could not find any element to create last elements connection");
            }

            if (index == filteredElements.size() - 1) {
                return;
            }

            ElementInfo nextLastElement = filteredElements.get(index + 1);
            repository.addGatewayClosingEntity(new Gateway(parallelBranch.getGatewayId() + "_end", null, branch.getType().toString(), processId, index + 1), nextLastElement.getId());
            for (var scope : parallelBranch.getChildrenIdsMap()) {
                if (!parallelBranch.hasScopeMessageElements(scope)) {
                    String lastScopeElementId = parallelBranch.findLastElementinScope(scope);
                    connections.add(
                        new ConnectionBPMNEntity(lastScopeElementId, parallelBranch.getGatewayId() + "_end", ConnectionType.SEQUENCE, null, indexDefinition++)
                    );
                }
            }
            connections.add(
                new ConnectionBPMNEntity(parallelBranch.getGatewayId() + "_end", nextLastElement.getId(), ConnectionType.SEQUENCE, null, indexDefinition++)
            );
        }
    }

    private boolean areIdsFromDifferentBranches(String currentId, String nextId, ElementBranch branch) {
        if (branch.getType() == BranchType.EXCLUSIVE) {
            ElementExclusiveBranch exclusiveBranch = (ElementExclusiveBranch) branch;
            BranchOrder currentBranch = exclusiveBranch.getIdInsideBranches(currentId);
            BranchOrder nextBranch = exclusiveBranch.getIdInsideBranches(nextId);
            return currentBranch != nextBranch;
        }
        if (branch.getType() == BranchType.PARALLEL) {
            ElementParallelBranch parallelBranch = (ElementParallelBranch) branch;
            return parallelBranch.areIdsFromDifferentBranches(currentId, nextId);
        }
        return false;
    }

    private void handleConnectionsByBranchType(List<ConnectionBPMNEntity> connections, ElementBranch branch, int indexDefinition, ElementInfo current) {
        if (branch.getType() == BranchType.EXCLUSIVE) {
            handleExclusiveBranchCreation(connections, (ElementExclusiveBranch) branch, indexDefinition, current);
        }
        if (branch.getType() == BranchType.PARALLEL) {
            handleParallelBranchCreation(connections, (ElementParallelBranch) branch, indexDefinition, current);
        }
    }

    private void handleExclusiveBranchCreation(List<ConnectionBPMNEntity> connections, ElementExclusiveBranch branch, int indexDefinition, ElementInfo current) {
        if (!(branch.getYesChildrenIdsMap().isEmpty())) {
            String firstYes = branch.getFirstYesElement();
            connections.add(new ConnectionBPMNEntity(
                current.getId(), firstYes, ConnectionType.EXCLUSIVE, "true", indexDefinition++
            ));
        }

        if (!branch.getNoChildrenIdsMap().isEmpty()) {
            String firstNo = branch.getFirstNoElement();
            connections.add(new ConnectionBPMNEntity(
                current.getId(), firstNo, ConnectionType.EXCLUSIVE, "false", indexDefinition++
            ));
        }
    }

    private void handleParallelBranchCreation(List<ConnectionBPMNEntity> connections, ElementParallelBranch branch, int indexDefinition, ElementInfo current) {
        branch.throwIfHasEndEvent();
        if (!branch.getChildrenIdsMap().isEmpty()) {
            for (int i = 0; i < branch.getChildrenIdsMap().size(); i++) {
                String firstScopeElementId = branch.findFirstElementInScope(branch.getChildrenIdsMap().get(i));
                connections.add(new ConnectionBPMNEntity(
                    current.getId(), firstScopeElementId, ConnectionType.PARALLEL, null, indexDefinition++
                ));
            }
        }
    }

    private Optional<ElementBranch> findBranchForGateway(String gatewayId, List<ElementBranch> branches) {
        return branches.stream()
            .filter(b -> b instanceof ElementBranch)
            .map(b -> (ElementBranch) b)
            .filter(b -> b.getGatewayId().equals(gatewayId))
            .findFirst();
    } 


    private void throwIfEntitiesWereNotCreated(List<String> ids) {
        for (String id : ids) {
            Optional<CommonBPMNIdEntity> entity = repository.getEntityById(id);
            if (entity.isEmpty()) {
                throw new NotFoundException("Could not find entity with id: " + id);
            }
        }
    }
}

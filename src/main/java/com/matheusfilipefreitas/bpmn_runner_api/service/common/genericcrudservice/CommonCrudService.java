package com.matheusfilipefreitas.bpmn_runner_api.service.common.genericcrudservice;

public interface CommonCrudService<T, ID> {
    T findById(ID id);
    T save(T entity);
    void delete(ID id);
}

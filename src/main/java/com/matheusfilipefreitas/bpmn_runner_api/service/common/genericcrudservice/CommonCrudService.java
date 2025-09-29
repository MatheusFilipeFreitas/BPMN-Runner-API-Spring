package com.matheusfilipefreitas.bpmn_runner_api.service.common.genericcrudservice;

import java.util.List;

public interface CommonCrudService<T, ID> {
    List<T> findAll();
    T findById(ID id);
    void save(T entity);
}

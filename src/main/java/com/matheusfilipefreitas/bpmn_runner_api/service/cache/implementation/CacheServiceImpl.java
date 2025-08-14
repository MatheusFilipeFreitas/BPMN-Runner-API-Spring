//package com.matheusfilipefreitas.bpmn_runner_api.service.cache.implementation;
//
//import com.matheusfilipefreitas.bpmn_runner_api.service.cache.CacheService;
//import lombok.AllArgsConstructor;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.stereotype.Service;
//
//@AllArgsConstructor
//
//@Service
//public class CacheServiceImpl implements CacheService {
//
//    private final CacheManager cacheManager;
//
//    public boolean isDataFromCache(String key) {
//        Cache cache = cacheManager.getCache(key);
//        if (cache == null) {
//            return false;
//        }
//        Cache.ValueWrapper valueWrapper = cache.get(key);
//        return valueWrapper != null;
//    }
//}

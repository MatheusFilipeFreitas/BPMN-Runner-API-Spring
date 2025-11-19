package com.matheusfilipefreitas.bpmn_runner_api.configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        
        cacheManager.registerCustomCache("allowedOriginsCache", 
            Caffeine.newBuilder()

                .expireAfterAccess(5, TimeUnit.MINUTES) 
                
                .maximumSize(1)
                .build()
        );
        return cacheManager;
    }
}

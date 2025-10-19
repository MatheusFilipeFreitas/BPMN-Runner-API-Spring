package com.matheusfilipefreitas.bpmn_runner_api.security;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class AllowedOrigins {

    // "allowedOriginsCache" deve ser configurado na sua classe de @EnableCaching
    // Este método irá rodar e salvar o resultado no cache.
    // O cache será invalidado após 10 minutos (configurável).
    @Cacheable("allowedOriginsCache")
    public List<String> getAllowedOrigins() {
        try {
            Firestore db = FirestoreClient.getFirestore();
            
            // Supondo uma coleção "allowed_origins" onde cada doc tem um campo "url"
            return db.collection("allowed_origins").get().get()
                    .getDocuments()
                    .stream()
                    .map(doc -> (String) doc.get("url")) // Pega o campo 'url' do documento
                    .collect(Collectors.toList());
                    
        } catch (InterruptedException | ExecutionException e) {
            // Logue o erro e retorne uma lista vazia ou lance uma exceção
            System.out.println("Erro ao buscar origens permitidas do Firestore" + e.getMessage());
            return List.of(); // Retorna vazio em caso de falha
        }
    }

    @CacheEvict(value = "allowedOriginsCache", allEntries = true)
    public void refreshAllowedOriginsCache() {
        System.out.println("Cache de origens permitidas foi limpo.");
    }
}

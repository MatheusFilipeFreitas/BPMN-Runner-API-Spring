package com.matheusfilipefreitas.bpmn_runner_api.configuration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.google.cloud.firestore.Firestore;

@Component
public class FirebaseConnectionTest implements CommandLineRunner {
    
    @Value("${env.type:}")
    private String environmentConfig;

    private final Firestore firestore;

    public FirebaseConnectionTest(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void run(String... args) throws Exception {
        if (environmentConfig.equals("production")) {
            return;
        }
        System.out.println("🔍 Testando conexão com Firebase...");

        var docRef = firestore.collection("test").document("connection-check");
        docRef.set(Map.of("status", "ok", "timestamp", System.currentTimeMillis())).get();

        var snapshot = docRef.get().get();

        System.out.println("✅ Firebase conectado com sucesso!");
        System.out.println("📄 Dados lidos: " + snapshot.getData());
    }
}
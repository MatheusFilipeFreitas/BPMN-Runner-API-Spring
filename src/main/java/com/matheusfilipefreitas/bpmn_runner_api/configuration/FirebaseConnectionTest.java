package com.matheusfilipefreitas.bpmn_runner_api.configuration;

import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.google.cloud.firestore.Firestore;

@Component
public class FirebaseConnectionTest implements CommandLineRunner {

    private final Firestore firestore;

    public FirebaseConnectionTest(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔍 Testando conexão com Firebase...");

        // Tente gravar e ler um documento de teste
        var docRef = firestore.collection("test").document("connection-check");
        docRef.set(Map.of("status", "ok", "timestamp", System.currentTimeMillis())).get();

        var snapshot = docRef.get().get();

        System.out.println("✅ Firebase conectado com sucesso!");
        System.out.println("📄 Dados lidos: " + snapshot.getData());
    }
}
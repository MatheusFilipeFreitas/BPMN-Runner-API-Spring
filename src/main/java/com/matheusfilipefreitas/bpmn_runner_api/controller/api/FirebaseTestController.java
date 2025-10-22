package com.matheusfilipefreitas.bpmn_runner_api.controller.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.firestore.Firestore;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*") 
public class FirebaseTestController {

    private final Firestore firestore;

    public FirebaseTestController(Firestore firestore) {
        this.firestore = firestore;
    }

    @GetMapping("/firestore")
    public ResponseEntity<?> testFirestore() throws Exception {
        var docRef = firestore.collection("api-tests").document("hello-world");
        docRef.set(Map.of("msg", "Olá, Firebase!", "timestamp", System.currentTimeMillis())).get();
        var snapshot = docRef.get().get();
        return ResponseEntity.ok(snapshot.getData());
    }
}
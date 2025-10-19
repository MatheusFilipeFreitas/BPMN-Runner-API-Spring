package com.matheusfilipefreitas.bpmn_runner_api.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Configuration
public class FirestoreConfig {
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        try (InputStream serviceAccount = 
                getClass().getClassLoader().getResourceAsStream("firebase-service-account.json")) {

            if (serviceAccount == null) {
                throw new FileNotFoundException("firebase-service-account.json not found in resources/");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            return FirebaseApp.initializeApp(options);
        }
    }

    @Bean
    public Firestore firestore(FirebaseApp app) {
        return FirestoreClient.getFirestore(app);
    }
}

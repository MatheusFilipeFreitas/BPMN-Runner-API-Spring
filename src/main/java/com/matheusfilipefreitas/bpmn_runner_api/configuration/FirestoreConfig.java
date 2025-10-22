package com.matheusfilipefreitas.bpmn_runner_api.configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;

@Configuration
public class FirestoreConfig {

    @Value("${firebase.credentials:}")
    private String firebaseCredentials;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        FirebaseApp existingApp = getExistingFirebaseApp();
        if (existingApp != null) {
            return existingApp;
        }

        InputStream credentialsStream = getCredentialsInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public Firestore firestore(FirebaseApp app) {
        return FirestoreClient.getFirestore(app);
    }

    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp app) {
        return FirebaseAuth.getInstance(app);
    }

    private InputStream getCredentialsInputStream() throws IOException {
        String creds = System.getenv("FIREBASE_CREDENTIALS");

        if (creds != null && !creds.isBlank()) {
            return new ByteArrayInputStream(creds.getBytes(StandardCharsets.UTF_8));
        }

        if (firebaseCredentials != null && !firebaseCredentials.isBlank()) {
            return new ByteArrayInputStream(firebaseCredentials.getBytes(StandardCharsets.UTF_8));
        }

        throw new IOException("Firebase credentials not found in environment or application.properties");
    }

    private FirebaseApp getExistingFirebaseApp() {
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getApps().get(0);
        }
        return null;
    }
}

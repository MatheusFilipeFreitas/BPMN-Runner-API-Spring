package com.matheusfilipefreitas.bpmn_runner_api.configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;

@Configuration
public class FirestoreConfig {

    @Bean
    public FirebaseApp firebaseApp() throws Exception {
        InputStream credentialsStream = getCredentialsInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getApps().get(0);
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
        InputStream fileStream = getClass().getClassLoader()
                .getResourceAsStream("bpmn-runner-account.json");
        if (fileStream != null) {
            System.out.println("📄 Firebase credentials loaded from local file.");
            return fileStream;
        }

        System.out.println("🌍 Firebase credentials loaded from environment variables.");

        String base64Key = System.getenv("FIREBASE_PRIVATE_KEY");
        String privateKey = decodeBase64PrivateKey(base64Key);

        Map<String, Object> creds = Map.ofEntries(
                Map.entry("type", System.getenv("FIREBASE_TYPE")),
                Map.entry("project_id", System.getenv("FIREBASE_PROJECT_ID")),
                Map.entry("private_key_id", System.getenv("FIREBASE_PRIVATE_KEY_ID")),
                Map.entry("private_key", privateKey),
                Map.entry("client_email", System.getenv("FIREBASE_CLIENT_EMAIL")),
                Map.entry("client_id", System.getenv("FIREBASE_CLIENT_ID")),
                Map.entry("auth_uri", System.getenv("FIREBASE_AUTH_URI")),
                Map.entry("token_uri", System.getenv("FIREBASE_TOKEN_URI")),
                Map.entry("auth_provider_x509_cert_url", System.getenv("FIREBASE_AUTH_PROVIDER_CERT_URL")),
                Map.entry("client_x509_cert_url", System.getenv("FIREBASE_CLIENT_CERT_URL")),
                Map.entry("universe_domain", System.getenv("FIREBASE_UNIVERSE_DOMAIN"))
        );

        String json = new ObjectMapper().writeValueAsString(creds);
        return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    }

    private String decodeBase64PrivateKey(String base64Key) {
        if (base64Key == null || base64Key.isBlank()) {
            throw new IllegalStateException("FIREBASE_PRIVATE_KEY_BASE64 environment variable is missing.");
        }

        byte[] decodedBytes = Base64.getDecoder().decode(base64Key);
        String decoded = new String(decodedBytes, StandardCharsets.UTF_8);
        return decoded.replace("\\n", "\n");
    }
}

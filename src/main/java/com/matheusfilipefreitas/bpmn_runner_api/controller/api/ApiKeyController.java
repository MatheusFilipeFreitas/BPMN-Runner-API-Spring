package com.matheusfilipefreitas.bpmn_runner_api.controller.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.matheusfilipefreitas.bpmn_runner_api.model.api.ApiKeyRecord;
import com.matheusfilipefreitas.bpmn_runner_api.model.dto.CreateKeyRequest;
import com.matheusfilipefreitas.bpmn_runner_api.service.api.ApiKeyService;

@RestController
@Profile("!test")
@RequestMapping("/keys")
@CrossOrigin(origins = "*")
public class ApiKeyController {
    private final Firestore firestore;
    private final ApiKeyService apiKeyService;

    public ApiKeyController(Firestore firestore, ApiKeyService apiKeyService) {
        this.firestore = firestore;
        this.apiKeyService = apiKeyService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createKey(@AuthenticationPrincipal String uid, @RequestBody CreateKeyRequest req) throws Exception {
        ApiKeyRecord rec = apiKeyService.createApiKey(uid, req.allowedOrigins(), req.daysValid());
        return ResponseEntity.ok(rec);
    }

    @PostMapping("/renew/{keyId}")
    public ResponseEntity<?> renewKey(@AuthenticationPrincipal String uid, @PathVariable String keyId, @RequestParam long days) throws Exception {
        ApiKeyRecord rec = apiKeyService.renewKey(uid, keyId, days);
        return ResponseEntity.ok(rec);
    }

    @GetMapping
    public ResponseEntity<?> listKeys(@AuthenticationPrincipal String uid) throws ExecutionException, InterruptedException {
        if (uid == null || uid.isBlank() || uid.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }

        CollectionReference col = firestore.collection("users").document(uid).collection("apiKeys");
        ApiFuture<QuerySnapshot> future = col.get();
        List<QueryDocumentSnapshot> docs = future.get().getDocuments();

        List<ApiKeyRecord> keys = docs.stream()
                .map(doc -> {
                    ApiKeyRecord docData = doc.toObject(ApiKeyRecord.class);
                    String documentId = doc.getId();

                    ApiKeyRecord rec = new ApiKeyRecord(
                        documentId,
                        docData.getKey(),
                        docData.getCreatedAt(),
                        docData.getExpiresAt(),
                        docData.getRequestCount(),
                        docData.getAllowedOrigins()
                    );
                    return rec;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(keys);
    }
}

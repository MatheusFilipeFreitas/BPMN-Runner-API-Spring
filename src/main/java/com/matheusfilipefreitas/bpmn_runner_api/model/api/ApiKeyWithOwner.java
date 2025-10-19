package com.matheusfilipefreitas.bpmn_runner_api.model.api;

import com.google.cloud.firestore.DocumentReference;

public record ApiKeyWithOwner(
    ApiKeyRecord apiKeyRecord,
    String ownerId,
    DocumentReference documentReference
) {

}

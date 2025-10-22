package com.matheusfilipefreitas.bpmn_runner_api.configuration.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FirebaseKeyUtils {
    public static String decodeBase64PrivateKey(String base64Key) {
        if (base64Key == null || base64Key.isBlank()) {
            throw new IllegalArgumentException("Firebase private key (Base64) not provided");
        }

        byte[] decodedBytes = Base64.getDecoder().decode(base64Key);
        String decoded = new String(decodedBytes, StandardCharsets.UTF_8);
        return decoded.replace("\\n", "\n");
    }
}

package com.example.productapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    @Value("${security.api.key}")
    private String apiKey;

    @Operation(summary = "Get API key for testing")
    @PostMapping("/apikey")
    public ResponseEntity<Map<String, String>> getApiKey() {
        Map<String, String> response = new HashMap<>();
        response.put("apiKey", apiKey);
        response.put("message", "Use this API key in X-API-Key header");
        return ResponseEntity.ok(response);
    }
}
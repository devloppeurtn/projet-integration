package com.example.Service_Commande.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/commandes-fallback")
    public ResponseEntity<String> commandeFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service commandes est temporairement indisponible. Veuillez réessayer plus tard.");
    }
}

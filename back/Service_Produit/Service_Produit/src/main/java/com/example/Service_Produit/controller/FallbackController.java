package com.example.Service_Produit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/produits-fallback")
    public ResponseEntity<String> produitFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Service Produit est temporairement indisponible. Veuillez réessayer plus tard.");
    }
}


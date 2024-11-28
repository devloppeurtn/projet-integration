package com.example.Service_Panier.service;

import com.example.Service_Panier.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserServiceFallback implements UserServiceClient {
    @Override
    public UserDTO getUserById(String userId) {
        // Retourner un UserDTO avec des valeurs par défaut ou une gestion d'erreur
        return new UserDTO("default", "User", "default@example.com");
    }
}

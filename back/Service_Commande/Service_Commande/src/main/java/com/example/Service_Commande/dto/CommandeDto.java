package com.example.Service_Commande.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CommandeDto {
    // Getters et Setters
    private String userId;
    private List<ProduitQuantiteDto> produits;

}


package com.example.Service_Panier.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String name;
    private String email;


    public UserDTO(String aDefault, String user, String mail) {
    }
}


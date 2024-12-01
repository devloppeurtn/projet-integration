package com.example.moviefront.models

enum class UserRole(val role: String) {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    companion object {
        // Permet de récupérer un rôle à partir d'une chaîne
        fun fromString(role: String): UserRole? {
            return values().find { it.role == role }
        }
    }
}
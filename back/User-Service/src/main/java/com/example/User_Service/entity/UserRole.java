package com.example.User_Service.entity;

public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static UserRole fromString(String role) {
        for (UserRole userRole : values()) {
            if (userRole.role.equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        return null;
    }
}

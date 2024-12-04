package com.example.moviefront.models

data class PasswordChangeRequest(
    val email: String,
    val oldPassword: String,
    val newPassword: String
)


package com.example.moviefront.models

data class ResetPasswordRequest(
    val token: String,
    val newPassword: String
)

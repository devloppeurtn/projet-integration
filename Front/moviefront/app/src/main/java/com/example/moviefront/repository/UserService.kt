package com.example.moviefront.repository

import com.example.moviefront.models.ForgotPasswordRequest
import com.example.moviefront.models.LoginRequest
import com.example.moviefront.models.ResetPasswordRequest
import com.example.moviefront.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface UserService {

    @POST("users/register")
    fun registerUser(@Body user: User): Call<User>

    @POST("users/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<User>

    // Modification ici : envoi de l'email dans le corps de la requête
    @POST("users/forgot-password")
    fun forgotPassword(@Body forgotPasswordRequest: ForgotPasswordRequest): Call<Void>

    // Modification ici : envoi du token et du nouveau mot de passe dans le corps
    @POST("users/reset-password")
    fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Call<Void>
}
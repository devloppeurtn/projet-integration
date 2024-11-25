package com.example.moviefront.repository

import Movie
import com.example.moviefront.models.ForgotPasswordRequest
import com.example.moviefront.models.LoginRequest
import com.example.moviefront.models.ResetPasswordRequest
import com.example.moviefront.models.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface UserService {
    @DELETE("users/{email}/deletefavorite/{movieId}")
    fun removeFavoriteMovie(
        @Path("email") email: String,
        @Path("movieId") movieId: String
    ): Call<Void>

    @POST("users/subscribe")
    fun subscribeToPremium(@Header("User-Email") email: String): Call<User>

    @GET("users/{email}/favoritess")
    fun getFavoriteMovies(@Path("email") email: String): Call<List<Movie>>

    @POST("users/{email}/favoritesss")
    fun addToFavorites(
        @Path("email") email: String,
        @Query("movieId") movieId: String
    ): Call<Void>
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
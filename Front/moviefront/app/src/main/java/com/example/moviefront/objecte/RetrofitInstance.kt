package com.example.moviefront.objecte

import com.example.moviefront.repository.UserService
import com.example.moviefront.repository.ProductService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // URL pour le microservice des utilisateurs
    private const val BASE_URL_USER = "http://10.0.2.2:8082/api/"

    // URL pour le microservice des produits
    private const val BASE_URL_PRODUCT = "http://10.0.2.2:8088/api/"  // Assurez-vous que cette URL correspond à celle de votre service produit

    // Configuration Gson avec setLenient(true) pour accepter les JSON malformés
    private val gson = GsonBuilder()
        .setLenient() // Permet d'accepter des JSON malformés
        .create()

    // Instance Retrofit pour le service des utilisateurs
    val apiUser: UserService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_USER)
            .addConverterFactory(GsonConverterFactory.create(gson)) // Utilisation du Gson personnalisé
            .build()
            .create(UserService::class.java)
    }

    // Instance Retrofit pour le service des produits
    val apiProduct: ProductService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_PRODUCT)
            .addConverterFactory(GsonConverterFactory.create(gson)) // Utilisation du Gson personnalisé
            .build()
            .create(ProductService::class.java)
    }

    // URL pour le service des commandes
    private const val BASE_URL_ORDER = "http://10.0.2.2:8089/api/orders/"

    // Instance Retrofit pour le service des commandes
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_ORDER)
            .addConverterFactory(GsonConverterFactory.create(gson)) // Utilisation du Gson personnalisé
            .build()
    }
}

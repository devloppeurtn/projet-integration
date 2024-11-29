package com.example.moviefront.repository

import com.example.moviefront.Domian.Product
import retrofit2.Call
import retrofit2.http.GET

interface ProductService {
    @GET("produits") // L'URL de votre API backend
    fun getProducts(): Call<List<Product>> // Utilisation correcte de Call avec un type générique
}

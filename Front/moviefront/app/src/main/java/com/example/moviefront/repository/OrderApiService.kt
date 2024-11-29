package com.example.moviefront.repository
import com.example.moviefront.Domian.OrderRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
interface OrderApiService {
    @POST("neworder") // Assurez-vous que l'URL correspond à votre backend
    fun createOrder(@Body orderRequest: OrderRequest): Call<Void>
}
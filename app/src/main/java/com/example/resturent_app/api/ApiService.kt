package com.example.resturent_app.api

import com.example.resturent_app.models.OrderRequest
import com.example.resturent_app.models.ProductResponse
import com.example.resturent_app.models.RestaurantResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("v1/api/all-restaurants")
    fun getRestaurants(): Call<RestaurantResponse>

    @GET("v1/api/all-fooditems")
    fun getProducts(): Call<ProductResponse>

    // ERROR FIX HERE:
    // Must return Call<Void> so we can use .enqueue()
    @POST("v1/api/orders/place") // Make sure this path matches your backend route exactly
    fun placeOrder(@Body orderRequest: OrderRequest): Call<Void>
}
package com.example.resturent_app.api

import com.example.resturent_app.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("v1/api/restaurant/all-restaurants")
    fun getRestaurants(): Call<RestaurantResponse>

    @GET("v1/api/restaurant/all-fooditems")
    fun getProducts(): Call<ProductResponse>

    @POST("v1/api/orders/place")
    fun placeOrder(@Body orderRequest: OrderRequest): Call<Void>

    // Login API
    @POST("v1/api/user/login")
    fun login(@Body request: LoginRequest): Call<AuthResponse>

    // Register API (Ye Naya Hai)
    @POST("v1/api/user/register-user")
    fun register(@Body request: RegisterRequest): Call<AuthResponse>
    // 1. Place Order
    @POST("v1/api/order/checkout")
    fun placeOrder(@Body request: CheckoutRequest): Call<CheckoutResponse>

    // 2. Get User Orders
    @GET("v1/api/order/user/{userId}")
    fun getUserOrders(@Path("userId") userId: Int): Call<OrderHistoryResponse>
}
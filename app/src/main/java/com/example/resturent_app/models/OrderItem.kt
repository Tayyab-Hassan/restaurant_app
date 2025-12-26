package com.example.resturent_app.models

import com.google.gson.annotations.SerializedName

data class OrderItem(
    @SerializedName("productId") val productId: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("price") val price: Double
)
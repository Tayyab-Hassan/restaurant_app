package com.example.resturent_app.models

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("totalAmount") val totalAmount: Double,
    @SerializedName("address") val address: String,
    @SerializedName("items") val items: List<OrderItem>
)
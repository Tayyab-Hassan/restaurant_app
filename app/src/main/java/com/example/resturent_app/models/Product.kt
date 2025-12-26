package com.example.resturent_app.models

import java.io.Serializable
data class ProductResponse(
    val success: Boolean,
    val data: List<Product>
)

data class Product(
    val id: Int,
    val restaurantId: Int,
    val name: String,
    val description: String,
    val price: String,
    val imageUrl: String
) : Serializable
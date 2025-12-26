package com.example.resturent_app.models
import java.io.Serializable // Import this

data class RestaurantResponse(
    val success: Boolean,
    val data: List<Restaurant>
)

// Add ': Serializable' here
data class Restaurant(
    val id: Int,
    val name: String,
    val email: String
) : Serializable
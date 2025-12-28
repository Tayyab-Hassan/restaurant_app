package com.example.resturent_app.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

// --- CHECKOUT REQUEST MODELS ---

data class CheckoutRequest(
    val userId: Int,
    val phone: String,
    val deliveryAddress: String,
    val cartItems: List<CartItemRequest>
)

data class CartItemRequest(
    val foodItemId: Int, // The API calls it 'foodItemId', not 'productId'
    val quantity: Int
)

// --- CHECKOUT RESPONSE MODEL ---
data class CheckoutResponse(
    val success: Boolean,
    val statusCode: Int,
    val message: String?
)

// --- GET ALL ORDERS RESPONSE MODELS ---

data class OrderHistoryResponse(
    val success: Boolean,
    val statusCode: Int,
    val data: List<OrderData>
)

data class OrderData(
    val id: Int,
    val userId: Int,
    val restaurantId: Int,
    val deliveryAddress: String,
    val status: String, // "PENDING", "COMPLETED" etc.
    val totalPrice: String,
    val createdAt: String,
    val restaurant: RestaurantInfo?,
    val items: List<OrderItemDetail>
)

data class RestaurantInfo(
    val id: Int,
    val name: String,
    val email: String
)

data class OrderItemDetail(
    val id: Int,
    val foodName: String,
    val quantity: Int,
    val unitPrice: String
)
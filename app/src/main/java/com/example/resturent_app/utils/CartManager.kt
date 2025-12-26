package com.example.resturent_app.utils

import android.util.Log
import com.example.resturent_app.models.CartItem
import com.example.resturent_app.models.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object CartManager {
    // Local list to show on UI immediately
    private val cartItems = mutableListOf<CartItem>()

    // Firebase Instances
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Get current User ID
    private fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    // --- MAIN FUNCTIONS ---

    fun getCartItems(): List<CartItem> = cartItems

    fun addToCart(product: Product) {
        val existingItem = cartItems.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            cartItems.add(CartItem(product, 1))
        }

        // Sync with Firestore
        saveCartToFirestore()
    }

    fun decreaseQuantity(product: Product) {
        val existingItem = cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                existingItem.quantity--
            } else {
                cartItems.remove(existingItem)
            }
            // Sync with Firestore
            saveCartToFirestore()
        }
    }

    fun removeItem(product: Product) {
        cartItems.removeAll { it.product.id == product.id }
        // Sync with Firestore
        saveCartToFirestore()
    }

    fun clearCart() {
        cartItems.clear()
        saveCartToFirestore()
    }

    fun getTotalPrice(): Double {
        var total = 0.0
        for (item in cartItems) {
            val price = item.product.price.toDoubleOrNull() ?: 0.0
            total += (price * item.quantity)
        }
        return total
    }

    // --- FIRESTORE LOGIC ---

    // 1. Save the whole cart to Firestore (Overwrites existing cart)
    private fun saveCartToFirestore() {
        val userId = getCurrentUserId() ?: return // If not logged in, stop

        // We create a map where Key = Product ID, Value = CartItem
        // This is easier to save than a list
        val cartMap = hashMapOf<String, Any>()

        for (item in cartItems) {
            cartMap[item.product.id.toString()] = item
        }

        // Path: users/{uid}/cart_data/current_cart
        // We use .set() to overwrite the old cart with the new one
        db.collection("users").document(userId)
            .collection("cart_data").document("current_cart")
            .set(mapOf("items" to cartItems))
            .addOnSuccessListener {
                Log.d("CartManager", "Cart synced with Firestore")
            }
            .addOnFailureListener { e ->
                Log.e("CartManager", "Error syncing cart", e)
            }
    }

    // 2. Fetch Cart when App Starts
    fun fetchCartFromFirestore(onComplete: () -> Unit) {
        val userId = getCurrentUserId()
        if (userId == null) {
            onComplete()
            return
        }

        db.collection("users").document(userId)
            .collection("cart_data").document("current_cart")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Get the list of hash maps from Firestore
                    val rawList = document.get("items") as? List<HashMap<String, Any>>

                    if (rawList != null) {
                        cartItems.clear() // Clear local before adding remote data

                        for (map in rawList) {
                            // Manually reconstruct the CartItem and Product objects
                            // Because Firestore returns generic Maps
                            try {
                                val quantity = (map["quantity"] as Long).toInt()
                                val prodMap = map["product"] as HashMap<String, Any>

                                val product = Product(
                                    id = (prodMap["id"] as Long).toInt(),
                                    restaurantId = (prodMap["restaurantId"] as Long).toInt(),
                                    name = prodMap["name"] as String,
                                    description = prodMap["description"] as String,
                                    price = prodMap["price"] as String,
                                    imageUrl = prodMap["imageUrl"] as String
                                )

                                cartItems.add(CartItem(product, quantity))
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
                onComplete() // Tell UI we are done
            }
            .addOnFailureListener {
                onComplete()
            }
    }
}
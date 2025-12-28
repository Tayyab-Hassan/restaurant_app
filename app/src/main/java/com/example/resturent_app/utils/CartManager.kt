package com.example.resturent_app.utils

import android.content.Context
import android.widget.Toast
import com.example.resturent_app.models.CartItem
import com.example.resturent_app.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CartManager {
    // Ye list temporary RAM mein data rakhti hai
    private var cartItems = mutableListOf<CartItem>()

    // --- SAVE & LOAD (LOCAL STORAGE) ---

    // Data ko mobile mein save karna
    private fun saveCartToPrefs(context: Context) {
        val prefs = context.getSharedPreferences("my_cart_prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val gson = Gson()
        // List ko String (JSON) mein badal kar save karna
        val json = gson.toJson(cartItems)

        editor.putString("CART_DATA", json)
        editor.apply()
    }

    // App start hone par data wapis lana
    fun loadCartFromPrefs(context: Context) {
        val prefs = context.getSharedPreferences("my_cart_prefs", Context.MODE_PRIVATE)
        val json = prefs.getString("CART_DATA", null)

        if (json != null) {
            try {
                val gson = Gson()
                val type = object : TypeToken<MutableList<CartItem>>() {}.type
                cartItems = gson.fromJson(json, type)
            } catch (e: Exception) {
                // Agar data corrupted hai to crash mat karo, bas list clear kardo
                e.printStackTrace()
                cartItems = mutableListOf()
            }
        }
    }

    // --- ACTIONS ---

    fun getCartItems(): List<CartItem> = cartItems

    fun addToCart(context: Context, product: Product) {
        val existingItem = cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            cartItems.add(CartItem(product, 1))
        }
        saveCartToPrefs(context) // Save immediately
        Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()
    }

    fun decreaseQuantity(context: Context, product: Product) {
        val existingItem = cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                existingItem.quantity--
            } else {
                cartItems.remove(existingItem)
            }
            saveCartToPrefs(context) // Save immediately
        }
    }

    fun removeItem(context: Context, product: Product) {
        cartItems.removeAll { it.product.id == product.id }
        saveCartToPrefs(context) // Save immediately
    }

    fun clearCart(context: Context) {
        cartItems.clear()
        saveCartToPrefs(context)
    }

    fun getTotalPrice(): Double {
        var total = 0.0
        for (item in cartItems) {
            val price = item.product.price.toDoubleOrNull() ?: 0.0
            total += (price * item.quantity)
        }
        return total
    }
}
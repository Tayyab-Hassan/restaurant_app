package com.example.resturent_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.resturent_app.api.RetrofitClient
import com.example.resturent_app.models.OrderItem
import com.example.resturent_app.models.OrderRequest
import com.example.resturent_app.utils.CartManager
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val etAddress = findViewById<EditText>(R.id.etAddress)
        val tvTotal = findViewById<TextView>(R.id.tvTotalAmount)
        val btnPlaceOrder = findViewById<Button>(R.id.btnPlaceOrder)

        // Show Total
        val totalAmount = CartManager.getTotalPrice()
        tvTotal.text = "Total to Pay: $${String.format("%.2f", totalAmount)}"

        btnPlaceOrder.setOnClickListener {
            val address = etAddress.text.toString().trim()

            // Validation
            if (address.isEmpty()) {
                Toast.makeText(this, "Please enter address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call function to place order
            placeOrderToBackend(address, totalAmount)
        }
    }

    private fun placeOrderToBackend(address: String, total: Double) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
            return
        }

        // 1. Get Cart Items from Local Manager
        val cartItems = CartManager.getCartItems()

        // 2. Convert CartItem (Mobile Model) -> OrderItem (API Model)
        // Kotlin ka '.map' function Flutter ke '.map().toList()' jaisa hai
        val apiOrderItems = cartItems.map {
            OrderItem(
                productId = it.product.id, // Backend will use this to find the restaurant
                quantity = it.quantity,
                price = (it.product.price.toDoubleOrNull() ?: 0.0)
            )
        }

        // 3. Create the Main Request Object
        val request = OrderRequest(
            userId = userId,
            totalAmount = total,
            address = address,
            items = apiOrderItems
        )

        // 4. Send to API
        // .enqueue() is like .then() in Flutter Future
        RetrofitClient.instance.placeOrder(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Success!
                    Toast.makeText(this@CheckoutActivity, "Order Placed Successfully!", Toast.LENGTH_LONG).show()

                    CartManager.clearCart() // Clear local cart

                    // Navigate to Success Screen
                    val intent = Intent(this@CheckoutActivity, OrderSuccessActivity::class.java)
                    // Remove back stack so user can't go back to checkout
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@CheckoutActivity, "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CheckoutActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
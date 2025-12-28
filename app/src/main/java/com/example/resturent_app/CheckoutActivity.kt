package com.example.resturent_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.resturent_app.api.RetrofitClient
import com.example.resturent_app.models.CartItemRequest
import com.example.resturent_app.models.CheckoutRequest
import com.example.resturent_app.models.CheckoutResponse
import com.example.resturent_app.utils.CartManager
import com.example.resturent_app.utils.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutActivity : AppCompatActivity() {

    // UI Variables declare karein
    private lateinit var etPhone: EditText
    private lateinit var etAddress: EditText
    private lateinit var btnPlaceOrder: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        // --- FIX IS HERE (Explicit Type <Type> add kiya hai) ---
        etPhone = findViewById<EditText>(R.id.etPhone)
        etAddress = findViewById<EditText>(R.id.etAddress)
        tvTotal = findViewById<TextView>(R.id.tvTotalAmount)
        btnPlaceOrder = findViewById<Button>(R.id.btnPlaceOrder)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Show Total
        val totalAmount = CartManager.getTotalPrice()
        tvTotal.text = "Total to Pay: $${String.format("%.2f", totalAmount)}"

        btnPlaceOrder.setOnClickListener {
            // Ab .text error nahi dega kyunki humne upar <EditText> bata diya hai
            val phone = etPhone.text.toString().trim()
            val address = etAddress.text.toString().trim()

            // Validation
            if (phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call function
            placeOrder(phone, address)
        }
    }

    private fun placeOrder(phone: String, address: String) {
        val session = UserSession(this)
        val userIdStr = session.getUserId()

        // Convert String ID to Int (Safety check ke sath)
        val userId = userIdStr.toIntOrNull()

        if (userId == null || userId == -1) {
            Toast.makeText(this, "User not logged in correctly", Toast.LENGTH_SHORT).show()
            return
        }

        // 1. Start Loading
        showLoading(true)

        // 2. Prepare Data for API
        val cartItems = CartManager.getCartItems()

        // Convert Local Cart items to API Request format
        val apiCartItems = cartItems.map {
            CartItemRequest(
                foodItemId = it.product.id,
                quantity = it.quantity
            )
        }

        val request = CheckoutRequest(
            userId = userId,
            phone = phone,
            deliveryAddress = address,
            cartItems = apiCartItems
        )

        // 3. API Call
        RetrofitClient.instance.placeOrder(request).enqueue(object : Callback<CheckoutResponse> {
            override fun onResponse(call: Call<CheckoutResponse>, response: Response<CheckoutResponse>) {
                showLoading(false) // Stop Loading

                val body = response.body()

                // Check Success
                if (response.isSuccessful && body != null && body.success) {

                    // Clear Cart locally
                    CartManager.clearCart(this@CheckoutActivity)

                    Toast.makeText(this@CheckoutActivity, "Order Placed Successfully!", Toast.LENGTH_LONG).show()

                    // Navigate to Success Screen
                    val intent = Intent(this@CheckoutActivity, OrderSuccessActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this@CheckoutActivity, "Failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CheckoutResponse>, t: Throwable) {
                showLoading(false) // Stop Loading
                Toast.makeText(this@CheckoutActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Helper Function for Loading Animation
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            btnPlaceOrder.text = "" // Button text hide karo
            btnPlaceOrder.isEnabled = false // Button disable karo
            progressBar.visibility = View.VISIBLE // Spinner dikhao
        } else {
            btnPlaceOrder.text = "Place Order" // Text wapis lao
            btnPlaceOrder.isEnabled = true // Button enable karo
            progressBar.visibility = View.GONE // Spinner chupao
        }
    }
}
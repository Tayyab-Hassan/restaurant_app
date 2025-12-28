package com.example.resturent_app

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resturent_app.adapters.OrdersAdapter
import com.example.resturent_app.api.RetrofitClient
import com.example.resturent_app.models.OrderHistoryResponse
import com.example.resturent_app.utils.UserSession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrdersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)

        val rvOrders = findViewById<RecyclerView>(R.id.rvOrders)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        rvOrders.layoutManager = LinearLayoutManager(this)

        // Get User ID
        val session = UserSession(this)
        val userId = session.getUserId().toIntOrNull()

        if (userId == null || userId == -1) {
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Call API
        RetrofitClient.instance.getUserOrders(userId).enqueue(object : Callback<OrderHistoryResponse> {
            override fun onResponse(call: Call<OrderHistoryResponse>, response: Response<OrderHistoryResponse>) {
                progressBar.visibility = View.GONE
                val body = response.body()

                if (response.isSuccessful && body != null && body.success) {
                    val ordersList = body.data

                    if (ordersList.isNotEmpty()) {
                        rvOrders.adapter = OrdersAdapter(ordersList)
                    } else {
                        Toast.makeText(this@MyOrdersActivity, "No orders found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MyOrdersActivity, "Failed to load orders", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OrderHistoryResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MyOrdersActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
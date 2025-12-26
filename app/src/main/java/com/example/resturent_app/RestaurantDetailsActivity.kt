package com.example.resturent_app

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resturent_app.adapters.ProductAdapter
import com.example.resturent_app.api.RetrofitClient
import com.example.resturent_app.models.ProductResponse
import com.example.resturent_app.models.Restaurant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantDetailsActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter
    private lateinit var rvProducts: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)

        // 1. Get Restaurant Data
        val restaurant = intent.getSerializableExtra("restaurant_data") as? Restaurant
        val tvName = findViewById<TextView>(R.id.tvRestName)

        // Set Name
        if (restaurant != null) {
            tvName.text = restaurant.name
        }

        // 2. Setup RecyclerView
        rvProducts = findViewById(R.id.rvRestProducts)
        rvProducts.layoutManager = GridLayoutManager(this, 2)

        adapter = ProductAdapter(emptyList()) { product ->
            // Click on product -> Open Product Details
            val intent = Intent(this, ProductDetailsActivity::class.java)
            intent.putExtra("product_data", product)
            startActivity(intent)
        }
        rvProducts.adapter = adapter

        // 3. Fetch Products and Filter by this Restaurant ID
        fetchProducts(restaurant?.id ?: -1)
    }

    private fun fetchProducts(restaurantId: Int) {
        RetrofitClient.instance.getProducts().enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val allProducts = response.body()!!.data

                    // Filter: Sirf is restaurant ke products dikhao
                    val filteredList = allProducts.filter { it.restaurantId == restaurantId }
                    adapter.updateData(filteredList)
                }
            }
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(this@RestaurantDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
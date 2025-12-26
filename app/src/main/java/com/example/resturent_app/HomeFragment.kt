package com.example.resturent_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resturent_app.adapters.ProductAdapter
import com.example.resturent_app.adapters.RestaurantAdapter
import com.example.resturent_app.api.RetrofitClient
import com.example.resturent_app.models.Product
import com.example.resturent_app.models.ProductResponse
import com.example.resturent_app.models.RestaurantResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Fragment layout ko constructor mein pass karte hain
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var rvRestaurants: RecyclerView
    private lateinit var rvProducts: RecyclerView
    private lateinit var loader: ProgressBar
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var productAdapter: ProductAdapter
    private var allProducts: List<Product> = listOf()

    // Activity mein 'onCreate' hota hai, Fragment mein 'onViewCreated'
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View lookups (Note: view.findViewById use karein)
        rvRestaurants = view.findViewById(R.id.rvRestaurants)
        rvProducts = view.findViewById(R.id.rvProducts)
        loader = view.findViewById(R.id.loader)

        setupRecyclerViews()
        fetchData()
    }

    private fun setupRecyclerViews() {
        // Horizontal Restaurants
        rvRestaurants.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        restaurantAdapter = RestaurantAdapter(emptyList()) { restaurant ->
            val intent = Intent(context, RestaurantDetailsActivity::class.java)
            intent.putExtra("restaurant_data", restaurant)
            startActivity(intent)
        }
        rvRestaurants.adapter = restaurantAdapter

        // Grid Products
        rvProducts.layoutManager = GridLayoutManager(context, 2)
        productAdapter = ProductAdapter(emptyList()) { product ->
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("product_data", product)
            startActivity(intent)
        }
        rvProducts.adapter = productAdapter
    }

    private fun fetchData() {
        // ... (Same API logic as before, just replace 'this' with 'context' for Toasts) ...
        RetrofitClient.instance.getRestaurants().enqueue(object : Callback<RestaurantResponse> {
            override fun onResponse(call: Call<RestaurantResponse>, response: Response<RestaurantResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    restaurantAdapter.updateData(response.body()!!.data)
                }
            }
            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
                // Handle error
            }
        })

        RetrofitClient.instance.getProducts().enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                loader.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    allProducts = response.body()!!.data
                    productAdapter.updateData(allProducts)
                }
            }
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                loader.visibility = View.GONE
            }
        })
    }
}
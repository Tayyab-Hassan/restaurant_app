package com.example.resturent_app

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.resturent_app.models.Product

class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        // 1. Get Data from Intent
        // "product_data" wo key hai jo hum MainActivity se bhejenge
        val product = intent.getSerializableExtra("product_data") as? Product

        // 2. Initialize Views
        val img = findViewById<ImageView>(R.id.imgProductFull)
        val tvName = findViewById<TextView>(R.id.tvProductName)
        val tvPrice = findViewById<TextView>(R.id.tvProductPrice)
        val tvDesc = findViewById<TextView>(R.id.tvProductDesc)
        val btnAdd = findViewById<Button>(R.id.btnAddToCart)

        // 3. Set Data
        if (product != null) {
            tvName.text = product.name
            tvPrice.text = "$${product.price}"
            tvDesc.text = product.description

            Glide.with(this).load(product.imageUrl).into(img)

            btnAdd.setOnClickListener {
                com.example.resturent_app.utils.CartManager.addToCart(this, product)
            }
        }
    }
}
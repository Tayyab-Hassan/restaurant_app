package com.example.resturent_app

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.resturent_app.adapters.CartAdapter
import com.example.resturent_app.utils.CartManager

class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var adapter: CartAdapter
    private lateinit var tvTotal: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvCart = view.findViewById<RecyclerView>(R.id.rvCart)
        tvTotal = view.findViewById(R.id.tvTotal)
        val btnCheckout = view.findViewById<Button>(R.id.btnCheckout)

        // Setup RecyclerView
        rvCart.layoutManager = LinearLayoutManager(context)

        adapter = CartAdapter(CartManager.getCartItems()) {
            // This is the callback when Qty changes
            updateTotal()
        }
        rvCart.adapter = adapter

        updateTotal()

        // Checkout Button
        btnCheckout.setOnClickListener {
            if (CartManager.getCartItems().isEmpty()) {
                Toast.makeText(context, "Cart is Empty", Toast.LENGTH_SHORT).show()
            } else {
                val intent = android.content.Intent(requireContext(), com.example.resturent_app.CheckoutActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // Since this is a Fragment, if we go to Home and add items,
    // when we come back to Cart, we need to refresh the list.
    override fun onResume() {
        super.onResume()
        // Reload data every time the screen becomes visible
        adapter.updateData(CartManager.getCartItems())
        updateTotal()

    }

    private fun updateTotal() {
        val total = CartManager.getTotalPrice()
        tvTotal.text = "$${String.format("%.2f", total)}"
    }
}
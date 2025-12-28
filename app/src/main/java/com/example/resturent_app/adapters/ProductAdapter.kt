package com.example.resturent_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.resturent_app.R
import com.example.resturent_app.models.Product

class ProductAdapter(
    private var list: List<Product>,
    private val onProductClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.imgProduct)
        val name: TextView = view.findViewById(R.id.tvName)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val btnCart: ImageView = view.findViewById(R.id.btnAddCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name
        holder.price.text = "$${item.price}"

        // Load Image using Glide
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.ic_launcher_background) // Add a placeholder in drawables if you want
            .into(holder.image)

        holder.itemView.setOnClickListener { onProductClick(item) }
        holder.btnCart.setOnClickListener {
            // Handle Add to Cart
            com.example.resturent_app.utils.CartManager.addToCart(holder.itemView.context, item)
        }
    }

    override fun getItemCount() = list.size

    fun updateData(newList: List<Product>) {
        list = newList
        notifyDataSetChanged()
    }
}
package com.example.resturent_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.resturent_app.R
import com.example.resturent_app.models.CartItem
import com.example.resturent_app.utils.CartManager

class CartAdapter(
    private var list: List<CartItem>,
    private val onQuantityChange: () -> Unit // Callback to refresh UI
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgCart)
        val name: TextView = view.findViewById(R.id.tvCartName)
        val price: TextView = view.findViewById(R.id.tvCartPrice)
        val qty: TextView = view.findViewById(R.id.tvQty)
        val btnPlus: ImageView = view.findViewById(R.id.btnPlus)
        val btnMinus: ImageView = view.findViewById(R.id.btnMinus)
        val btnDelete: ImageView = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.product.name
        holder.qty.text = item.quantity.toString()

        // Calculate item total (Price * Qty)
        val unitPrice = item.product.price.toDoubleOrNull() ?: 0.0
        holder.price.text = "$${unitPrice * item.quantity}"

        Glide.with(holder.itemView.context).load(item.product.imageUrl).into(holder.img)

        // Logic for Buttons
        // PLUS BUTTON
        holder.btnPlus.setOnClickListener {
            com.example.resturent_app.utils.CartManager.addToCart(holder.itemView.context, item.product)
            notifyItemChanged(position)
            onQuantityChange()
        }

        // MINUS BUTTON
        holder.btnMinus.setOnClickListener {
            com.example.resturent_app.utils.CartManager.decreaseQuantity(holder.itemView.context, item.product)
            notifyDataSetChanged() // List refresh
            onQuantityChange()
        }

        // DELETE BUTTON
        holder.btnDelete.setOnClickListener {
            com.example.resturent_app.utils.CartManager.removeItem(holder.itemView.context, item.product)
            notifyDataSetChanged()
            onQuantityChange()
        }
    }

    override fun getItemCount() = list.size

    fun updateData(newList: List<CartItem>) {
        list = newList
        notifyDataSetChanged()
    }
}
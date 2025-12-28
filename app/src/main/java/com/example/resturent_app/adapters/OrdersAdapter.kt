package com.example.resturent_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.resturent_app.R
import com.example.resturent_app.models.OrderData

class OrdersAdapter(private val list: List<OrderData>) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvResName: TextView = view.findViewById(R.id.tvRestaurantName)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val tvPrice: TextView = view.findViewById(R.id.tvOrderPrice)
        val tvItems: TextView = view.findViewById(R.id.tvOrderItems)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = list[position]

        // Restaurant Name (Check null safety)
        holder.tvResName.text = order.restaurant?.name ?: "Unknown Restaurant"

        holder.tvStatus.text = order.status
        holder.tvPrice.text = "$${order.totalPrice}"

        // Show only first 10 chars of date (YYYY-MM-DD)
        if (order.createdAt.length >= 10) {
            holder.tvDate.text = order.createdAt.substring(0, 10)
        } else {
            holder.tvDate.text = order.createdAt
        }

        // Format Items List (e.g. "2x Burger, 1x Pasta")
        val itemsSummary = StringBuilder()
        for (item in order.items) {
            itemsSummary.append("${item.quantity}x ${item.foodName}, ")
        }
        holder.tvItems.text = itemsSummary.toString().trimEnd(',', ' ')
    }

    override fun getItemCount() = list.size
}
package com.example.resturent_app.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.resturent_app.R
import com.example.resturent_app.models.Restaurant

class RestaurantAdapter(
    private var list: List<Restaurant>,
    private val onRestaurantClick: (Restaurant) -> Unit // Callback function
) : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    // To track selected item (for UI highlighting)
    private var selectedPosition = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvRestaurantName)
        val container: LinearLayout = view.findViewById(R.id.container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name

        // Logic to highlight selected restaurant (Purple vs Grey)
        if (selectedPosition == position) {
            holder.container.setBackgroundColor(Color.parseColor("#673AB7")) // Purple
            holder.name.setTextColor(Color.WHITE)
        } else {
            holder.container.setBackgroundColor(Color.parseColor("#F0F0F0")) // Grey
            holder.name.setTextColor(Color.BLACK)
        }

        holder.itemView.setOnClickListener {
            val previousItem = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousItem)
            notifyItemChanged(selectedPosition)

            onRestaurantClick(item)
        }
    }

    override fun getItemCount() = list.size

    fun updateData(newList: List<Restaurant>) {
        list = newList
        notifyDataSetChanged()
    }
}
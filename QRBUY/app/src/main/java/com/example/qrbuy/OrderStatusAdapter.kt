package com.example.qrbuy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qrbuy.ModelClass

class OrderStatusAdapter(private val data: List<ModelClass>) : RecyclerView.Adapter<OrderStatusAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_orderstatus, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemText: TextView = itemView.findViewById(R.id.ItemText)
        private val itemPrice: TextView = itemView.findViewById(R.id.ItemPrice)
        private val itemImage: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(item: ModelClass) {
            itemText.text = item.url
            itemPrice.text = "$"+item.price.toString()

            Glide.with(itemView.context)
                .load(item.url)
                .into(itemImage)
        }
    }
}

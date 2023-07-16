package com.example.qrbuy

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class OrdersItemsAdapter(private val items: List<ModelClass>) : RecyclerView.Adapter<OrdersItemsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderStatus: TextView = itemView.findViewById(R.id.Orderstatus)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val orderDateTime: TextView = itemView.findViewById(R.id.Orderdatetime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_orders, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val db = FirebaseFirestore.getInstance()
        val currentItem = items[position]
        val transactionRef = db.collection("Users").document(currentItem.userId).collection("transactionNames").document(currentItem.transactionID?:"")
        transactionRef.get()
            .addOnSuccessListener {documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val status = documentSnapshot.getBoolean("status")
                    if(status != null && status)
                    {
                        holder.orderStatus.setText("ORDER PLACED")
                    }
                    else{
                        holder.orderStatus.setText("ORDER FAILED")
                    }
                }
            }
        Glide.with(holder.itemView.context)
            .load(currentItem.url)
            .into(holder.imageView)
        holder.orderDateTime.text = currentItem.transactionDate

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, TransactionActivity::class.java)
            intent.putExtra("UserId", currentItem.userId)
            intent.putExtra("TransactionId", currentItem.transactionID)
            intent.putExtra("TransactionDate", currentItem.transactionDate)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }
}

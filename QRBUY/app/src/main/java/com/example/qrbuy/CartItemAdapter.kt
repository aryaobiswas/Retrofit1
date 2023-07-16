package com.example.qrbuy

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class CartItemAdapter(
    context: Context,
    private val items: ArrayList<ModelClass>,
    private val totalpriceTextView: TextView
    ) :
    ArrayAdapter<ModelClass>(context, 0, items) {

    private var totalPrice: Float = 0.0f

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val holder: ViewHolder

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.cart_item_row, parent, false)
            holder = ViewHolder(itemView)
            itemView.tag = holder
        } else {
            holder = itemView.tag as ViewHolder
        }

        val item = items[position]
        Log.d("received item", items.toString())

        Glide.with(context)
            .load(item.url)
            .into(holder.itemImage)

        holder.title.text = "Text: ${item.url}"

        holder.price.text = "$${item.price}"

        holder.deleteButton.setOnClickListener {
            deleteItem(item)
        }

        return itemView!!
    }

    private fun deleteItem(item: ModelClass) {
        val db = FirebaseFirestore.getInstance()
        val UserId: String = item.userId
        val DBlocation = db.collection("Users").document(UserId).collection("cartItems")
        DBlocation
            .whereEqualTo("id", item.id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!task.result.isEmpty) {
                        val documentSnapshot = task.result.documents[0]
                        val documentId = documentSnapshot.id

                        DBlocation
                            .document(documentId)
                            .delete()
                            .addOnSuccessListener {
                                removeItem(item)
                                recalculateTotalPrice()
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firestore", "Error deleting item", e)
                            }
                    }
                }
            }
    }

    private fun recalculateTotalPrice() {
        totalPrice = 0.0f
        for (item in items) {
            totalPrice += item.price ?: 0.0f
        }
        totalpriceTextView.text = "$$totalPrice"
    }

    private fun removeItem(item: ModelClass) {
        items.remove(item)
        notifyDataSetChanged()
    }

    private class ViewHolder(view: View) {
        val itemImage: ImageView = view.findViewById(R.id.imageView)
        val title: TextView = view.findViewById(R.id.titleTextView)
        val price: TextView = view.findViewById(R.id.priceTextView)
        val deleteButton: Button = view.findViewById(R.id.deletebutton)
    }

}

package com.example.qrbuy

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class RecyclerViewAdapter(private val context: Context, private val data: ArrayList<ModelClass>) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val cartItems = ArrayList<ModelClass>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val itemCard: CardView = itemView.findViewById(R.id.ItemCard)
        val itemImg: ImageView = itemView.findViewById(R.id.itemImage)
        val title: TextView = itemView.findViewById(R.id.text_title)
        val price: TextView = itemView.findViewById(R.id.text_price)
//        val originalprice: TextView = itemView.findViewById(R.id.original_price)
//        val discount: TextView = itemView.findViewById(R.id.text_discount)
        val addtoCart: Button = itemView.findViewById(R.id.addtocartbutton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_row, parent, false))
    }

    override fun getItemCount(): Int {
        return  data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = data[position]
        val UserId: String = model.userId
        Log.d("UserID", UserId)
        holder.price.text = "$"+model.price.toString()
        Log.d("price", holder.price.text as String)

        Glide.with(context)
            .load(model.url)
            .into(holder.itemImg)

        val db = FirebaseFirestore.getInstance()

        holder.itemCard.setOnClickListener {
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra("item", model)
            context.startActivity(intent)
        }

        val DBlocation = db.collection("Users").document(UserId).collection("cartItems")
        DBlocation
            .whereEqualTo("id", model.id) // Assuming "id" is the field to check for uniqueness
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.isEmpty) {
                        holder.addtoCart.isSelected = false
                        holder.addtoCart.text = "Add to Cart"
                    } else {
                        holder.addtoCart.isSelected = true
                        holder.addtoCart.text = "Added to Cart"
                    }
                }
            }

        holder.addtoCart.setOnClickListener {
//            val DBlocation = db.collection("Users").document(UserId).collection("cartItems")
            DBlocation
                .whereEqualTo("id", model.id) // Assuming "id" is the field to check for uniqueness
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result.isEmpty) {
                            addItemToCart(db, model)
                            holder.addtoCart.isSelected = true
                            holder.addtoCart.text = "Added to Cart"
                        }
                        else {
                            // Item already exists, delete it first
                            val documentSnapshot = task.result.documents[0]
                            val documentId = documentSnapshot.id

                            DBlocation
                                .document(documentId)
                                .delete()
                                .addOnSuccessListener {
                                    holder.addtoCart.isSelected = false
                                    holder.addtoCart.text = "Add to Cart"
                                }
                                .addOnFailureListener { e ->
                                    // Handle delete operation failure
                                    Log.e("Firestore", "Error deleting item", e)
                                }
                        }
                    }
                }

            Log.d("selected item", model.toString())
        }

    }

    private fun addItemToCart(db: FirebaseFirestore, model: ModelClass) {
        val UserId: String = model.userId
        val DBlocation = db.collection("Users").document(UserId).collection("cartItems")
        DBlocation
            .add(model)
            .addOnSuccessListener { documentReference ->
                Log.d("Firebase", "Item added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error adding item", e)
            }
    }

}
package com.example.qrbuy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class OrdersActivity : AppCompatActivity() {

    private val data: ArrayList<ModelClass> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrdersItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)

        val UserID = intent.getStringExtra("UserID") ?: ""

        recyclerView = findViewById(R.id.ordersView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = OrdersItemsAdapter(data) // Empty string for initial datetime
        recyclerView.adapter = adapter

        val db = FirebaseFirestore.getInstance()

        val collectionRef = db.collection("Users").document(UserID).collection("transactionNames")

        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val documentId = document.id.toString()
                    var datetime = document.getString("datetime") ?: ""
                    Log.d("date", datetime)
                    val itemsCollectionRef = db.collection("Users").document(UserID).collection("transaction").document(documentId).collection("items")
                    itemsCollectionRef.limit(1).get()
                        .addOnSuccessListener { itemsQuerySnapshot ->
                            val itemsList = itemsQuerySnapshot.toObjects(ModelClass::class.java)
                            data.addAll(itemsList)
                            adapter = OrdersItemsAdapter(data)
                            recyclerView.adapter = adapter
                        }
                        .addOnFailureListener { e ->
                            // Handle any errors that occurred while retrieving the items
                        }
                }
            }
            .addOnFailureListener { e ->
                // Handle any errors that occurred while retrieving the data
            }
    }
}

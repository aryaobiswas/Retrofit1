package com.example.qrbuy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class OrderStatusActivity : AppCompatActivity() {

    private val data: ArrayList<ModelClass> = ArrayList()
    private lateinit var recyclerViewItems: RecyclerView
    private lateinit var adapter: OrderStatusAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_status)

        val orderStatus = intent.getBooleanExtra("status", false)
        val transactionId = intent.getStringExtra("TransactionId")
        val transactionDate = intent.getStringExtra("TransactionDate")
        val userID = intent.getStringExtra("UserId")

        val orderStatusTextView: TextView = findViewById(R.id.Orderstatus)
        val transactionIdTextView: TextView = findViewById(R.id.transactionIdTextView)
        val transactionDateTextView: TextView = findViewById(R.id.transactionDateTextView)
        val totalPriceTextView: TextView = findViewById(R.id.totalprice)
        recyclerViewItems = findViewById(R.id.transactionView)

        recyclerViewItems.layoutManager = LinearLayoutManager(this)
        adapter = OrderStatusAdapter(data)
        recyclerViewItems.adapter = adapter

        transactionIdTextView.text = transactionId ?: ""
        transactionDateTextView.text = transactionDate ?: ""

        val db = FirebaseFirestore.getInstance()
        var totalPrice: Float = 0.0f

        if (orderStatus) {
            orderStatusTextView.text = "ORDER PLACED"
        } else {
            orderStatusTextView.text = "ORDER FAILED"
        }

        if (transactionId != null && userID != null) {
            val transactionRef = db.collection("Users").document(userID).collection("transactionNames").document(transactionId)
            transactionRef.set(hashMapOf("status" to orderStatus))

            val itemsCollectionRef = db.collection("Users").document(userID).collection("transaction").document(transactionId).collection("items")
            itemsCollectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val itemsList = querySnapshot.toObjects(ModelClass::class.java)
                    for (item in itemsList) {
                        totalPrice += item.price ?: 0.0f
                    }
                    data.addAll(itemsList)
                    adapter.notifyDataSetChanged()
                    totalPriceTextView.text = "$" + totalPrice.toString()
                }
                .addOnFailureListener { e ->
                    // Handle any errors that occurred while retrieving the items
                }
        } else {
            // Handle the case when transactionId or userID is null
        }
    }
}

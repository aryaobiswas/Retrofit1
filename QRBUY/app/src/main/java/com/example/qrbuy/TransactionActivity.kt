package com.example.qrbuy

import TransactionsItemsAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class TransactionActivity : AppCompatActivity() {

    private val data: ArrayList<ModelClass> = ArrayList()
    private lateinit var recyclerViewItems: RecyclerView
    private lateinit var adapter: TransactionsItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        val transactionId = intent.getStringExtra("TransactionId")
        val transactionDate = intent.getStringExtra("TransactionDate")
        val userID = intent.getStringExtra("UserId")

        val transactionIdTextView: TextView = findViewById(R.id.transactionIdTextView)
        val transactionDateTextView: TextView = findViewById(R.id.transactionDateTextView)
        val totalPriceTextView: TextView = findViewById(R.id.totalprice)
        val orderStatus: TextView = findViewById(R.id.Orderstatus)
        recyclerViewItems = findViewById(R.id.transactionView)

        recyclerViewItems.layoutManager = LinearLayoutManager(this)
        adapter = TransactionsItemsAdapter(data)
        recyclerViewItems.adapter = adapter

        transactionIdTextView.text = transactionId ?: ""
        transactionDateTextView.text = transactionDate ?: ""

        val db = FirebaseFirestore.getInstance()
        var totalPrice: Float = 0.0f

        if (transactionId != null && userID != null) {
            val transactionRef = db.collection("Users").document(userID).collection("transactionNames").document(transactionId)
            transactionRef.get()
                .addOnSuccessListener {documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val status = documentSnapshot.getBoolean("status")
                        if(status != null && status)
                        {
                            orderStatus.setText("ORDER PLACED")
                        }
                        else{
                            orderStatus.setText("ORDER FAILED")
                        }
                    }
                }

            val itemsCollectionRef = db.collection("Users").document(userID).collection("transaction").document(transactionId).collection("items")
            itemsCollectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val itemsList = querySnapshot.toObjects(ModelClass::class.java)
                    for (item in itemsList) {
                        totalPrice += item.price ?: 0.0f
                    }
                    data.addAll(itemsList)
                    adapter.notifyDataSetChanged()
                    totalPriceTextView.text = "$"+totalPrice.toString()
                }
                .addOnFailureListener { e ->
                    // Handle any errors that occurred while retrieving the items
                }
        } else {
            // Handle the case when transactionId or userID is null
        }
    }
}

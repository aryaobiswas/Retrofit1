package com.example.qrbuy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import kotlin.random.Random

class CartActivity : AppCompatActivity() {

    private val cartItems: ArrayList<ModelClass> = ArrayList()
    private lateinit var listView: ListView
    private lateinit var totalprice: TextView
    private lateinit var checkoutButton: Button
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        backButton = findViewById(R.id.backBtn)
        listView = findViewById(R.id.cartListView)
        totalprice = findViewById(R.id.totalPriceTextView)
        checkoutButton = findViewById(R.id.checkoutButton)

        val UserID = intent.getStringExtra("UserID") ?: ""

        backButton.setOnClickListener{
            finish()
        }

        retrieveCartItems(UserID)

    }

    private fun retrieveCartItems(UserID: String) {
        var total: Float = 0.0f
        val db = FirebaseFirestore.getInstance()
        val DBlocation = db.collection("Users").document(UserID).collection("cartItems")
        DBlocation
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                for (document in querySnapshot) {
                    val item = document.toObject(ModelClass::class.java)
                    total += item.price ?: 0.0f
                    cartItems.add(item)
                }
                displayCartItems()
                recalculateTotalPrice(total)
//                totalprice.text = "$"+total.toString()

                checkoutButton.setOnClickListener {
                    copyCartItemsToTransaction(UserID)
                }
            }
            .addOnFailureListener { e ->
                Log.w("firebase fail", "Error retrieving items", e)
            }
    }

    private fun displayCartItems() {
        val adapter = CartItemAdapter(this, cartItems, totalprice)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun recalculateTotalPrice(total: Float) {
        totalprice.text = "$" + total.toString()
    }

    private fun copyCartItemsToTransaction(UserID: String) {
        val db = FirebaseFirestore.getInstance()
        val cartItemsRef = db.collection("Users").document(UserID).collection("cartItems")
        val datetime = getCurrentDateTime()
        val rand = Random.nextInt(10000000, 100000000).toString()
        val transactionRef = db.collection("Users").document(UserID).collection("transaction").document(rand)

        cartItemsRef.get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val batch = db.batch()

                for (document in querySnapshot) {
                    val item = document.toObject(ModelClass::class.java)
                    item.transactionDate=datetime
                    item.transactionID=rand
                    val newItemRef = transactionRef.collection("items").document(document.id)
                    batch.set(newItemRef, item)
                }

                batch.commit()
                    .addOnSuccessListener {
                        Log.d("firebase success", "Cart items copied to transaction successfully.")

                        // Store the rand value in the "transactionNames" collection
                        val transactionNamesRef = db.collection("Users").document(UserID).collection("transactionNames").document(rand)
                        val data = hashMapOf<String, Any>("datetime" to datetime)
                        transactionNamesRef.set(data)
                            .addOnSuccessListener {
                                Log.d("firebase success", "Rand value stored in transactionNames collection.")
                                val intent = Intent(this, PaymentActivity::class.java)
                                intent.putExtra("UserId", UserID)
                                intent.putExtra("TransactionId", rand)
                                intent.putExtra("TransactionDate", datetime)
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                Log.w("firebase fail", "Error storing rand value in transactionNames collection", e)
                            }

                        // Perform any necessary actions after copying the items
                    }
                    .addOnFailureListener { e ->
                        Log.w("firebase fail", "Error copying cart items to transaction", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w("firebase fail", "Error retrieving cart items", e)
            }
    }


    private fun getCurrentDateTime(): String {
        val currentDate = Date()

        // Format the date and time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formattedDate = dateFormat.format(currentDate)

        return formattedDate
    }

}

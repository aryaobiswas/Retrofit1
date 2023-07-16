package com.example.qrbuy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class ItemActivity : AppCompatActivity() {

    private lateinit var itemTitle: TextView
    private lateinit var itemImageView: ImageView
    private lateinit var itemPrice: TextView
    private lateinit var itemaddtoCart: Button
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        val item = intent.getSerializableExtra("item") as? ModelClass
        val db = FirebaseFirestore.getInstance()

        backButton = findViewById(R.id.backBtn)
        itemTitle = findViewById(R.id.itemTitle)
        itemPrice = findViewById(R.id.itemPrice)
        itemImageView = findViewById(R.id.imageView)
        itemaddtoCart = findViewById(R.id.addtocartbutton)

        if (item != null) {
            itemTitle.setText("Text: "+item.url)
            itemPrice.setText("$"+item.price)
            Glide.with(this)
                .load(item.url)
                .into(itemImageView)

            val DBlocation = db.collection("Users").document(item.userId).collection("cartItems")
            DBlocation
                .whereEqualTo("id", item.id) // Assuming "id" is the field to check for uniqueness
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result.isEmpty) {
                            itemaddtoCart.isSelected = false
                            itemaddtoCart.text = "Add to Cart"
                        } else {
                            itemaddtoCart.isSelected = true
                            itemaddtoCart.text = "Added to Cart"
                        }
                    }
                }

            itemaddtoCart.setOnClickListener{
                val DBlocation = db.collection("Users").document(item.userId).collection("cartItems")
                DBlocation
                    .whereEqualTo("id", item.id) // Assuming "id" is the field to check for uniqueness
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (task.result.isEmpty) {
                                addItemToCart(db, item)
                                itemaddtoCart.isSelected = true
                                itemaddtoCart.text = "Added to Cart"
                            }
                            else {
                                // Item already exists, delete it first
                                val documentSnapshot = task.result.documents[0]
                                val documentId = documentSnapshot.id

                                DBlocation
                                    .document(documentId)
                                    .delete()
                                    .addOnSuccessListener {
                                        itemaddtoCart.isSelected = false
                                        itemaddtoCart.text = "Add to Cart"
                                    }
                                    .addOnFailureListener { e ->
                                        // Handle delete operation failure
                                        Log.e("Firestore", "Error deleting item", e)
                                    }
                            }
                        }
                    }

                Log.d("selected item", item.toString())
            }

            backButton.setOnClickListener{
                finish()
            }

        }
    }

    private fun addItemToCart(db: FirebaseFirestore, model: ModelClass) {
        val UserId: String = model.userId
        val DBlocation = db.collection("Users").document(UserId).collection("cartItems")
        DBlocation
            .add(model)
            .addOnSuccessListener { documentReference ->
                setResult(RESULT_OK)
                Log.d("Firebase", "Item added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error adding item", e)
            }
    }

}

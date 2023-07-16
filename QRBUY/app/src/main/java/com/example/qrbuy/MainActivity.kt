package com.example.qrbuy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private val data: ArrayList<ModelClass> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private var UserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UserID = intent.getStringExtra("UserID") ?: ""

        recyclerView = findViewById(R.id.RecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        recyclerViewAdapter = RecyclerViewAdapter(this, data)
        recyclerView.adapter = recyclerViewAdapter

        fetchDataFromFirestore()

        val cartButton: ImageButton = findViewById(R.id.cartButton)
        cartButton.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("UserID", UserID)
            startActivity(intent)
        }

        val menuButton: ImageButton = findViewById(R.id.menuButton)
        menuButton.setOnClickListener {
            val intent = Intent(this, OrdersActivity::class.java)
            intent.putExtra("UserID", UserID)
            startActivity(intent)
        }
    }

    private fun fetchDataFromFirestore() {
        val db = Firebase.firestore

        db.collection("QRBUY")
            .get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    data.clear()
                    for (document in result) {
                        val id = document.get("id") as Long
                        val url = document.get("url") as String
                        val price = (document.get("price") as Double).toFloat()
                        val model = ModelClass(UserID, price, id, url, "", "", null)
                        data.add(model)
                    }

                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { e ->
                Log.w("oh shit", "Error adding document", e)
            }
    }
}

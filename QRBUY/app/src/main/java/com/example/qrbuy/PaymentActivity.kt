package com.example.qrbuy

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class PaymentActivity : AppCompatActivity() {

    private lateinit var transactionComplete: Button
    private lateinit var transactionIncomplete: Button
    private lateinit var amount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val transactionId = intent.getStringExtra("TransactionId")
        val transactionDate = intent.getStringExtra("TransactionDate")
        val userID = intent.getStringExtra("UserId")

        transactionComplete = findViewById(R.id.transactioncomplete)
        transactionIncomplete = findViewById(R.id.transactionincomplete)

        transactionComplete.setOnClickListener {
            val status = true
            returnResult(status,userID,transactionId,transactionDate)
        }

        transactionIncomplete.setOnClickListener {
            val status = false
            returnResult(status,userID,transactionId,transactionDate)
        }
    }

    private fun returnResult(status: Boolean, userID: String?, transactionId: String?, transactionDate: String?) {
        val resultIntent = Intent(this, OrderStatusActivity::class.java)
        resultIntent.putExtra("status", status)
        resultIntent.putExtra("UserId", userID)
        resultIntent.putExtra("TransactionId", transactionId)
        resultIntent.putExtra("TransactionDate", transactionDate)
        startActivity(resultIntent)
    }


}
package com.example.qrbuy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class UserLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        var UserID: String;
        val user1Button = findViewById<Button>(R.id.user1button)
        val user2Button = findViewById<Button>(R.id.user2button)
        val user3Button = findViewById<Button>(R.id.user3button)
        val user4Button = findViewById<Button>(R.id.user4button)

        user1Button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            UserID = "user1";
            intent.putExtra("UserID", UserID)
            startActivity(intent)
        }
        user2Button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            UserID = "user2";
            intent.putExtra("UserID", UserID)
            startActivity(intent)
        }
        user3Button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            UserID = "user3";
            intent.putExtra("UserID", UserID)
            startActivity(intent)
        }
        user4Button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            UserID = "user4";
            intent.putExtra("UserID", UserID)
            startActivity(intent)
        }

    }
}
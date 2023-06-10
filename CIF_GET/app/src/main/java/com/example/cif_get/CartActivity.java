package com.example.cif_get;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ListView cartListView;
    private TextView totalPriceTextView;
    private Button checkoutButton;

    private ArrayList<ModelClass> cartItems;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ModelClass cart = (ModelClass) getIntent().getSerializableExtra("cart");

        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        checkoutButton = findViewById(R.id.checkoutButton);

        cartItems = new ArrayList<>();

        if (cart != null) {
            ArrayList<ModelClass> cartItems = new ArrayList<>();
            cartItems.add(cart);

            cartListView = findViewById(R.id.cartListView);
            // Create an ArrayAdapter to display the cart items in the ListView
            CartAdapter adapter = new CartAdapter(this, cartItems);
            Log.d("Aryaadap", (adapter.getItem(0)).toString());
            cartListView.setAdapter(adapter);

//            calculateTotalPrice();
        }
    }

    private void calculateTotalPrice() {
        totalPrice = 0;

        for (ModelClass item : cartItems) {
            // Assuming the price is stored as a String, you may need to parse it to double if necessary
            double itemPrice = Double.parseDouble(item.getPrice());
            totalPrice += itemPrice;
        }

        // Display the total price
        totalPriceTextView.setText(String.valueOf(totalPrice));
    }

}
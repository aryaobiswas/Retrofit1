package com.example.cif_get;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private ListView cartListView;
    private TextView totalPriceTextView;
    private Button checkoutButton;
    private Button delete, add;

    FirebaseFirestore db;

    private ArrayList<ModelClass> cartItems;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = FirebaseFirestore.getInstance();

        ModelClass cart = (ModelClass) getIntent().getSerializableExtra("item");

        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        checkoutButton = findViewById(R.id.checkoutButton);
        cartListView = findViewById(R.id.cartListView);

        totalPrice = 0.0;

        cartItems = new ArrayList<>();

        if (cart != null && cart.getId() != null) {
            // Check if the item already exists in the database
            db.collection("cartList")
                    .whereEqualTo("id", cart.getId())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    // Item doesn't exist, add it to the database
                                    cart.setCount(1);
                                    db.collection("cartList")
                                            .add(cart)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d("Firestore", "Adding item");
                                                    retrieveCartItems();
                                                }
                                            });
                                } else {
                                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                    String documentId = documentSnapshot.getId();

                                    int currentCount = documentSnapshot.getLong("count").intValue();
                                    int updatedCount = currentCount + 1;

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("count", updatedCount);

                                    db.collection("cartList")
                                            .document(documentId)
                                            .update(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("Firestore", "Count updated for duplicate item");
                                                    retrieveCartItems();
                                                }
                                            });
                                }
                            } else {
                                Log.e("Firestore", "Error checking item existence", task.getException());
                            }
                        }
                    });
        } else {
            retrieveCartItems();
        }
    }

    private void retrieveCartItems() {
        db.collection("cartList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            cartItems.clear();
                            totalPrice = 0.0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ModelClass cartItem = document.toObject(ModelClass.class);
                                cartItems.add(cartItem);

                                String itemPricedollar = cartItem.getPrice();
                                String itemPrice = itemPricedollar.substring(1, itemPricedollar.length());
                                totalPrice = totalPrice + Double.parseDouble(itemPrice) * (cartItem.getCount());
                            }

                            totalPriceTextView.setText(String.valueOf(totalPrice));
                            updateListView();
                        } else {
                            Log.e("Firestore", "Error retrieving cart items", task.getException());
                        }
                    }
                });
    }

    private void updateListView() {
        CartAdapter adapter = new CartAdapter(CartActivity.this, cartItems, totalPrice, totalPriceTextView);
        cartListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

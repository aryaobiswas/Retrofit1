package com.example.cif_get;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends ArrayAdapter<ModelClass> {
    private LayoutInflater inflater;
    private FirebaseFirestore db;
    private double totalPrice;
    private ArrayList<ModelClass> cartItems;
    private TextView totalPriceTextView;

    public CartAdapter(Context context, ArrayList<ModelClass> cartItems, double totalPrice, TextView totalPriceTextView) {
        super(context, 0, cartItems);
        inflater = LayoutInflater.from(context);
        db = FirebaseFirestore.getInstance();
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
        this.totalPriceTextView = totalPriceTextView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }
        ModelClass cartItem = getItem(position);

        String itemId = cartItem.getId();
        String itemTitle = cartItem.getTitle();

        Button delete = convertView.findViewById(R.id.subtractButton);
        Button add = convertView.findViewById(R.id.addButton);
        TextView itemCountTextView = convertView.findViewById(R.id.countTextView);

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView priceTextView = convertView.findViewById(R.id.priceTextView);
        TextView brandTextView = convertView.findViewById(R.id.brandTextView);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        itemCountTextView.setText(String.valueOf(cartItem.getCount()));
        titleTextView.setText(cartItem.getTitle());
        priceTextView.setText(cartItem.getPrice());
        brandTextView.setText(cartItem.getBrand());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItemCount(cartItem, -1);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItemCount(cartItem, 1);
            }
        });

        Glide.with(getContext())
                .load(cartItem.getThumbnail())
                .into(imageView);

        return convertView;
    }

    private void updateItemCount(ModelClass cartItem, int countChange) {
        String itemId = cartItem.getId();
        int currentCount = cartItem.getCount();
        int updatedCount = currentCount + countChange;

        if (updatedCount < 0) {
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("count", updatedCount);

        if (updatedCount == 0) {
            // Delete the item from the database
            db.collection("cartList")
                    .whereEqualTo("id", itemId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                String documentId = documentSnapshot.getId();
                                db.collection("cartList")
                                        .document(documentId)
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("Firestore", "Item deleted");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Firestore", "Error deleting item", e);
                                            }
                                        });
                            }
                        }
                    });
        } else {
            // Update the count in the database
            db.collection("cartList")
                    .whereEqualTo("id", itemId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                String documentId = documentSnapshot.getId();
                                db.collection("cartList")
                                        .document(documentId)
                                        .update(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("Firestore", "Count updated for item");
                                                // Retrieve the updated data
                                                updateCartItemCount(cartItem, updatedCount);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("Firestore", "Error updating count", e);
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    public void updateCartItemCount(ModelClass cartItem, int count) {
        cartItem.setCount(count);

        if (count == 0) {
            // Remove the item from the cartItems list
            cartItems.remove(cartItem);
        }

        notifyDataSetChanged();

        totalPrice = 0.0;
        for (ModelClass item : cartItems) {
            String itemPricedollar = item.getPrice();
            String itemPrice = itemPricedollar.substring(1, itemPricedollar.length());
            totalPrice += Double.parseDouble(itemPrice) * item.getCount();
        }

        totalPriceTextView.setText(String.valueOf(totalPrice));
    }


}

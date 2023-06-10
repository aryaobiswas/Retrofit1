package com.example.cif_get;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<ModelClass> {
    private LayoutInflater inflater;

    public CartAdapter(Context context, ArrayList<ModelClass> cartItems) {
        super(context, 0, cartItems);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("AryaAdap1", "started");
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            Log.d("AryaAdap2", "null");
        }
        ModelClass cartItem = getItem(position);
        Log.d("AryaAdap3", cartItem.getTitle());

        String idTextView = String.valueOf(cartItem.getId());

        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView priceTextView = convertView.findViewById(R.id.priceTextView);
        TextView brandTextView = convertView.findViewById(R.id.brandTextView);
//        TextView idTextView = convertView.findViewById(R.id.idTextView);
        ImageView imageView = convertView.findViewById(R.id.imageView);

        titleTextView.setText(cartItem.getTitle());
        priceTextView.setText(cartItem.getPrice());
        brandTextView.setText(cartItem.getBrand());
//        idTextView.setText(String.valueOf(cartItem.getId()));
        Glide.with(getContext())
                .load(cartItem.getThumbnail())
                .into(imageView);

        return convertView;
    }
}


package com.example.cif_get;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImagesAdapter extends ArrayAdapter<String> {
    private LayoutInflater inflater;

    public ImagesAdapter(Context context, List<String> imageUrls) {
        super(context, 0, imageUrls);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(R.layout.item_image, parent, false);
        }

        String imageUrl = getItem(position);

        ImageView imageView = itemView.findViewById(R.id.imageView);

        Log.d("Arya4", "onResponse: " + imageUrl);

        Glide.with(getContext())
                .load(imageUrl)
                .into(imageView);

        return itemView;
    }

}

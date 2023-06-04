package com.example.ecom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ItemActivity extends AppCompatActivity {

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        ModelClass item = (ModelClass) getIntent().getSerializableExtra("item");

        Log.d("Arya1", "onResponse: " + "Model class working");

        TextView title, price, brand, description, discount, rating;
        ImageView imagesImageView = findViewById(R.id.item_images);

        title = findViewById(R.id.item_title);
        price = findViewById(R.id.item_price);
        brand = findViewById(R.id.item_brand);
        discount = findViewById(R.id.item_discount);
        rating = findViewById(R.id.item_rating);
        description = findViewById(R.id.item_description);
        imagesImageView = findViewById(R.id.item_images);

        if (item != null) {
            title.setText(item.getTitle());
            price.setText(String.valueOf(item.getPrice()));
            brand.setText(item.getBrand());
            discount.setText(String.valueOf(item.getDiscountPercentage()));
            rating.setText(String.valueOf(item.getRating()));
            description.setText(item.getDescription());

            String imagesString = item.getImages();
            imagesString = imagesString.substring(1, imagesString.length() - 1);
            String[] imageUrls = imagesString.replaceAll("\\\\/", "/").replaceAll("\"", "").split(",");

            Log.d("Arya3", "onResponse: " + "Pre condition working");

            for (int i = 0; i < imageUrls.length; i++)
            {
                if (imageUrls.length > 0) {
                    String imageUrl = imageUrls[i];

                    //                images.setText(imageUrl);
                    Glide.with(this)
                            .load(imageUrl)
                            .into(imagesImageView);
                    Log.d("Arya3", "onResponse: " + imageUrl);
                }
            }


        } else {
            Log.d("Arya-sad", "onResponse: " + "NULLLLL");
            title.setText(null);
        }
    }
}
package com.example.cif_get;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        ModelClass item = (ModelClass) getIntent().getSerializableExtra("item");

        TextView title, price, brand, description, discount, rating, originaprice;
        Button addtocart;
        ImageSlider imageSlider;
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        RatingBar ratingBar;

        title = findViewById(R.id.item_title);
        price = findViewById(R.id.item_price);
        brand = findViewById(R.id.item_brand);
        discount = findViewById(R.id.item_discount);
        originaprice = findViewById(R.id.original_price);
        addtocart = findViewById(R.id.addtoCart);
        rating = findViewById(R.id.item_rating);
        description = findViewById(R.id.item_description);
        imageSlider = findViewById(R.id.image_slider);

        if (item != null) {
            title.setText(item.getTitle());
            originaprice.setText(String.valueOf(item.getPrice()));
            brand.setText(item.getBrand());
            discount.setText(String.valueOf(item.getDiscountPercentage())+"% off");
            rating.setText(String.valueOf(item.getRating())+" / 5");
            description.setText(item.getDescription());
            float discountf = Float.parseFloat(item.getPrice().substring(1)) * (1 - Float.parseFloat(item.getDiscountPercentage())/ 100.0f);
            price.setText("$"+Integer.toString((int)discountf));

            float storedRating = Float.parseFloat(String.valueOf(item.getRating()));
            ratingBar = findViewById(R.id.rating_Bar);
            ratingBar.setRating(storedRating);

            String imagesString = item.getImages();
            imagesString = imagesString.substring(1, imagesString.length() - 1);
            String[] imageUrls = imagesString.replaceAll("\\\\/", "/").replaceAll("\"", "").split(",");

            Log.d("Arya3", "onResponse: " + "Pre condition working");

            for (int i = 0; i < imageUrls.length; i++)
            {
                slideModels.add(new SlideModel(imageUrls[i], ScaleTypes.FIT));
            }
            imageSlider.setImageList(slideModels, ScaleTypes.FIT);

            addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ModelClass cart = item;
                    Intent intent = new Intent(ItemActivity.this, CartActivity.class);
                    intent.putExtra("item", item);
                    startActivity(intent);
                }
            });


        } else {
            Log.d("Arya-sad", "onResponse: " + "NULLLLL");
            title.setText(null);
        }
    }
}
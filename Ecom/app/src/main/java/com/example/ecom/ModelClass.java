package com.example.ecom;

import org.json.JSONObject;

import java.io.Serializable;

public class ModelClass implements Serializable {

    JSONObject jsonObject;

    String title, brand, thumbnail, description, price, stock, discountPercentage, rating, images;

    public String getTitle() {
        return title;
    }

    public String getBrand() {
        return brand;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getStock() {
        return stock;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public String getRating() {
        return rating;
    }

    public String getImages() {
        return images;
    }

    public ModelClass(String title, String brand, String thumbnail, String description, String price, String stock, String discountPercentage, String rating, String images) {
        this.title = title;
        this.brand = brand;
        this.thumbnail = thumbnail;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.discountPercentage = discountPercentage;
        this.rating = rating;
        this.images = images;
    }
}

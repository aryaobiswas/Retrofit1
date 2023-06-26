package com.example.cif_get;

import org.json.JSONObject;

import java.io.Serializable;

public class ModelClass implements Serializable {

    JSONObject jsonObject;

    String id;
    String title;
    String brand;
    String thumbnail;
    String description;
    String price;
    String stock;
    String discountPercentage;
    String rating;
    String images;
    int count;

    public ModelClass() {
        // Empty constructor
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getThumbnail() { return thumbnail; }

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

    public ModelClass(String id, String title, String brand, String thumbnail, String description, String price, String stock, String discountPercentage, String rating, String images) {
        this.id = id;
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

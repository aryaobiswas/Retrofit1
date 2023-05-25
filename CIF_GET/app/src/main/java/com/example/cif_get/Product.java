package com.example.cif_get;

public class Product {

    private int id, stock;
    private String title,description, brand, category, thumbnail, images;
    private float price, discountPercentage, rating;

    public int getId() {
        return id;
    }

    public int getStock() {
        return stock;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getImages() {
        return images;
    }

    public float getPrice() {
        return price;
    }

    public float getDiscountPercentage() {
        return discountPercentage;
    }

    public float getRating() {
        return rating;
    }

}

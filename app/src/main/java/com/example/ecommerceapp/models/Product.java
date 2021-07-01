package com.example.ecommerceapp.models;

public class Product {
    private String Category, name, Price, image;

    public Product(){}

    public Product(String category, String name, String price, String image) {
        this.Category = category;
        this.name = name;
        this.Price = price;
        this.image = image;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        this.Category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

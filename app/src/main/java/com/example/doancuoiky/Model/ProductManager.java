package com.example.doancuoiky.Model;

public class ProductManager {
    private int id;
    private String name;
    private String createdDate;
    private String price;
    private int stock;
    private boolean isVisible;
    private String imageResId;

    // Constructor
    public ProductManager(int id, String name, String createdDate, String price, int stock, boolean isVisible, String imageResId) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.price = price;
        this.stock = stock;
        this.isVisible = isVisible;
        this.imageResId = imageResId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getImageResId() {
        return imageResId;
    }
}

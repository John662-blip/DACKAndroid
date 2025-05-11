package com.example.doancuoiky.Model;

public class MyProduct {
    private String imageResId;
    private String name;
    private double price;
    private String createdDate;
    private int quantity;
    private int idProduct;

    public MyProduct(int idProduct,String imageResId, String name, double price, String createdDate, int quantity) {
        this.idProduct = idProduct;
        this.imageResId = imageResId;
        this.name = name;
        this.price = price;
        this.createdDate = createdDate;
        this.quantity = quantity;
    }
    public int getIdProduct(){
        return  idProduct;
    }
    public void setIdProduct(int idProduct){
        this.idProduct = idProduct;
    }
    public String getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public int getQuantity() {
        return quantity;
    }
}

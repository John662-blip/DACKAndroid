package com.example.doancuoiky.Model;

public class Product {
    private int idProduct;
    private String name;
    private int quantity;
    private String image; // tên file ảnh hoặc URL
    private Double price;

    public Product(int idProduct, String name, int quantity, String image, Double price) {
        this.idProduct = idProduct;
        this.name = name;
        this.quantity = quantity;
        this.image = image;
        this.price = price;
    }

    public int getIdProduct() { return idProduct; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getImage() { return image; }
    public Double getPrice() { return price; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}

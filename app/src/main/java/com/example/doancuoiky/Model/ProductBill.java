package com.example.doancuoiky.Model;

public class ProductBill {
    private Long id;
    private String name;
    private int quantity;
    private double price;
    private String url;

    public ProductBill(Long id, String name, int quantity, double price,String url) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.url = url;
    }
    public String getImage(){
        return url;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

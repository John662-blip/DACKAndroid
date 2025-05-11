package com.example.doancuoiky.Model;

public class PlantModel {
    private int id;
    private String name;
    private int quantity;
    private String price;
    private String urlImage;

    public PlantModel(int id, String name, int quantity, String price,String urlImage) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.urlImage = urlImage;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    // Getter và Setter
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getPrice() { return price; }

    public void setPrice(String price) { this.price = price; }
}


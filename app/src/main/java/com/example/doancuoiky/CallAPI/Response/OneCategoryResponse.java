package com.example.doancuoiky.CallAPI.Response;

import java.util.List;

public class OneCategoryResponse {
    private boolean status;
    private String message;
    private OneCategoryResponse.Category body;

    // No‑args constructor
    public OneCategoryResponse() {}

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public OneCategoryResponse.Category getBody() { return body; }
    public void setBody(OneCategoryResponse.Category body) { this.body = body; }

    public static class Category {
        private int id;
        private String imageUrl;
        private String name;
        private List<OneCategoryResponse.Category.Product> products;

        public Category() {}

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<OneCategoryResponse.Category.Product> getProducts() { return products; }
        public void setProducts(List<OneCategoryResponse.Category.Product> products) { this.products = products; }

        public static class Product {
            private int id;
            private String imageUrl;
            private String productName;
            private int stockCount;
            private int soldCount;
            private String description;
            private int temperature;
            private int price;
            private String createDate;
            private int status;

            // map JSON field "humadity" → Java field humidity
            private float humadity;

            public Product() {}

            public int getId() { return id; }
            public void setId(int id) { this.id = id; }

            public String getImageUrl() { return imageUrl; }
            public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

            public String getProductName() { return productName; }
            public void setProductName(String productName) { this.productName = productName; }

            public int getStockCount() { return stockCount; }
            public void setStockCount(int stockCount) { this.stockCount = stockCount; }

            public int getSoldCount() { return soldCount; }
            public void setSoldCount(int soldCount) { this.soldCount = soldCount; }

            public String getDescription() { return description; }
            public void setDescription(String description) { this.description = description; }

            public int getTemperature() { return temperature; }
            public void setTemperature(int temperature) { this.temperature = temperature; }

            public int getPrice() { return price; }
            public void setPrice(int price) { this.price = price; }

            public String getCreateDate() { return createDate; }
            public void setCreateDate(String createDate) { this.createDate = createDate; }

            public int getStatus() { return status; }
            public void setStatus(int status) { this.status = status; }

            public float getHumidity() { return humadity; }
            public void setHumidity(int humidity) { this.humadity = humidity; }
        }
    }
}

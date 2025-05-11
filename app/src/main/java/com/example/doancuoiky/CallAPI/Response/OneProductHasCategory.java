package com.example.doancuoiky.CallAPI.Response;

import com.google.gson.annotations.SerializedName;

public class OneProductHasCategory {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("body")
    private OneProductHasCategory.Product body;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public OneProductHasCategory.Product getBody() {
        return body;
    }
    public static class Product {
        @SerializedName("id")
        private int id;

        @SerializedName("imageUrl")
        private String imageUrl;

        @SerializedName("productName")
        private String productName;

        @SerializedName("categoryId")
        private Long categoryId;

        @SerializedName("stockCount")
        private int stockCount;

        @SerializedName("soldCount")
        private int soldCount;

        @SerializedName("description")
        private String description;

        @SerializedName("temperature")
        private float temperature;

        @SerializedName("price")
        private int price;

        @SerializedName("createDate")
        private String createDate;

        @SerializedName("status")
        private int status;

        @SerializedName("humadity")
        private float humadity;

        // Getters and optionally setters
        public int getId() {
            return id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getProductName() {
            return productName;
        }

        public int getStockCount() {
            return stockCount;
        }

        public int getSoldCount() {
            return soldCount;
        }

        public String getDescription() {
            return description;
        }

        public float getTemperature() {
            return temperature;
        }

        public int getPrice() {
            return price;
        }

        public String getCreateDate() {
            return createDate;
        }

        public int getStatus() {
            return status;
        }

        public float getHumadity() {
            return humadity;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }
    }
}

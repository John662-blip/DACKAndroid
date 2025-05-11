package com.example.doancuoiky.CallAPI.Response;

import com.example.doancuoiky.CallAPI.Response.genera.ProductBody;

public class ProductPageResponse {
    private boolean status;
    private String message;
    private ProductBody body;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ProductBody getBody() {
        return body;
    }

    public void setBody(ProductBody body) {
        this.body = body;
    }
}

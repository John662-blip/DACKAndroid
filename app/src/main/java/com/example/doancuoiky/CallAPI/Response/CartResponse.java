package com.example.doancuoiky.CallAPI.Response;

import com.example.doancuoiky.CallAPI.Response.genera.Cart;

public class CartResponse {
    private boolean status;
    private String message;
    private Cart body;

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

    public Cart getBody() {
        return body;
    }

    public void setBody(Cart body) {
        this.body = body;
    }
}

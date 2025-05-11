package com.example.doancuoiky.CallAPI.Response;

import com.example.doancuoiky.CallAPI.Response.genera.Bill;

public class BillResponse {
    private boolean status;
    private String message;
    private Bill body;

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

    public Bill getBody() {
        return body;
    }

    public void setBody(Bill body) {
        this.body = body;
    }
}

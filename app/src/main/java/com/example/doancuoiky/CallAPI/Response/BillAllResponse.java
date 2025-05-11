package com.example.doancuoiky.CallAPI.Response;

import com.example.doancuoiky.CallAPI.Response.genera.Bill;

import java.util.List;

public class BillAllResponse {
    private boolean status;
    private String message;
    private List<Bill> body;

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

    public List<Bill> getBody() {
        return body;
    }

    public void setBody(List<Bill> body) {
        this.body = body;
    }
}

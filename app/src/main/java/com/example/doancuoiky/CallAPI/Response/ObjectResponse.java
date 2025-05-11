package com.example.doancuoiky.CallAPI.Response;

import java.util.List;

public class ObjectResponse {
    private boolean status;
    private String message;
    private List<List<Object>> body;

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

    public List<List<Object>> getBody() {
        return body;
    }

    public void setBody(List<List<Object>> body) {
        this.body = body;
    }
}

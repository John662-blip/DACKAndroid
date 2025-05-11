package com.example.doancuoiky.CallAPI.Response;

public class CountResponse {
    private boolean status;
    private String message;
    private Long body;

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

    public Long getBody() {
        return body;
    }

    public void setBody(Long body) {
        this.body = body;
    }
}

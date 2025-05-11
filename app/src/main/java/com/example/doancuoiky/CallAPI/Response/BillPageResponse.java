package com.example.doancuoiky.CallAPI.Response;

import com.example.doancuoiky.CallAPI.Response.genera.BillPage;
import com.example.doancuoiky.CallAPI.Response.genera.ProductBody;

public class BillPageResponse {
    private boolean status;
    private String message;
    private BillPage body;

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

    public BillPage getBody() {
        return body;
    }

    public void setBody(BillPage body) {
        this.body = body;
    }
}

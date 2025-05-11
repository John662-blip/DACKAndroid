package com.example.doancuoiky.CallAPI.Response.genera;
import java.util.List;

public class Bill {
    private int id;
    private String address;
    private String createdAt;
    private List<BillDetail> billDetails;
    private Account account;
    private long total;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<BillDetail> getBillDetails() {
        return billDetails;
    }

    public void setBillDetails(List<BillDetail> billDetails) {
        this.billDetails = billDetails;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

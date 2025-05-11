package com.example.doancuoiky.Model;

public class Order {
    private int orderId;
    private String date;
    private double totalPrice;
    private int status; // 0: Chờ xác nhận, 1: Đang giao hàng, 2: Đã hoàn thành, 3: Hủy đơn

    public Order(int orderId, String date, double totalPrice, int status) {
        this.orderId = orderId;
        this.date = date;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getDate() {
        return date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusText() {
        switch (status) {
            case 0: return "Chờ xác nhận";
            case 1: return "Đang giao hàng";
            case 2: return "Đã hoàn thành";
            case 3: return "Hủy đơn";
            default: return "Không rõ";
        }
    }
}


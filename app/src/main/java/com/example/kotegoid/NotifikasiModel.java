package com.example.kotegoid;

import java.util.List;

public class NotifikasiModel {
    private String order_id;
    private String status;
    private String user_id;
    private long total_bayar;
    private long timestamp;
    private List<CartItem> items; // List item yang dibeli

    public NotifikasiModel() {} // Wajib untuk Firebase

    public String getOrder_id() { return order_id; }
    public void setIdPesanan(String id) { this.order_id = id; }

    public String getStatus() { return status; }
    public String getUser_id() { return user_id; }
    public long getTotal_bayar() { return total_bayar; }
    public long getTimestamp() { return timestamp; }
    public List<CartItem> getItems() { return items; }
}
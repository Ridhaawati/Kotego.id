package com.example.kotegoid;

import java.util.List;

public class NotifikasiModel {
    private String order_id;
    private String status;
    private String user_id;
    private long total_bayar;
    private long timestamp;
    private List<CartItem> items; // Pastikan class CartItem juga sudah ada

    public NotifikasiModel() {} // Wajib untuk Firebase

    // Getter dan Setter
    public String getOrder_id() {
        return order_id;
    }

    // METHOD INI YANG DICARI OLEH PesananAdmin.java
    public void setIdPesanan(String id) {
        this.order_id = id;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUser_id() { return user_id; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    public long getTotal_bayar() { return total_bayar; }
    public void setTotal_bayar(long total_bayar) { this.total_bayar = total_bayar; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }

    // Method tambahan untuk RiwayatLaporan agar tidak error
    public String getHarga() {
        return String.valueOf(total_bayar);
    }
}
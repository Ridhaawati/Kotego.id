package com.example.kotegoid;

public class RiwayatPesananModel {
    public String orderId;
    public String tanggal;
    public String waktu;
    public String status;
    public int totalPrice;
    public long timestamp;

    public RiwayatPesananModel() {
    }

    public RiwayatPesananModel(String orderId, String tanggal, String waktu,
                               String status, int totalPrice, long timestamp) {
        this.orderId = orderId;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.status = status;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
    }

    // Getter untuk warna status
    public int getStatusColor() {
        switch (status) {
            case "Selesai":
                return 0xFF4CAF50; // Green
            case "Pending":
                return 0xFFFFA726; // Orange
            case "Dibatalkan":
                return 0xFFE53935; // Red
            default:
                return 0xFF757575; // Gray
        }
    }

    // Getter untuk background warna status
    public int getStatusBgColor() {
        switch (status) {
            case "Selesai":
                return 0xFFE8F5E9; // Light Green
            case "Pending":
                return 0xFFFFF3E0; // Light Orange
            case "Dibatalkan":
                return 0xFFFFEBEE; // Light Red
            default:
                return 0xFFF5F5F5; // Light Gray
        }
    }
}
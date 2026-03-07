package com.example.kotegoid;

public class OrderItemModel {
    public String namaMenu;
    public int jumlah;
    public int harga;
    public String imageUrl;

    public OrderItemModel() {
    }

    public OrderItemModel(String namaMenu, int jumlah, int harga, String imageUrl) {
        this.namaMenu = namaMenu;
        this.jumlah = jumlah;
        this.harga = harga;
        this.imageUrl = imageUrl;
    }
}
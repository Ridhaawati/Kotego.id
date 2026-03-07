package com.example.kotegoid;

public class StrukItemModel {
    public String namaItem;
    public int qty;
    public int harga;

    public StrukItemModel() {
    }

    public StrukItemModel(String namaItem, int qty, int harga) {
        this.namaItem = namaItem;
        this.qty = qty;
        this.harga = harga;
    }
}
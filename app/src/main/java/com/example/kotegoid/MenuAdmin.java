package com.example.kotegoid;

import com.google.firebase.database.PropertyName;

public class MenuAdmin {
    private String menu_id;
    private String admin_id; // Tambahan dari ERD (FK ke Admin)
    private String menu_name;
    private long price; // Pakai long supaya bisa dijumlahkan di Laporan
    private String category;
    private int stok;   // Tambahan dari ERD
    private String discription; // Sesuai typo di ERD kamu: "discription"
    private String image_url;

    // Constructor kosong wajib untuk Firebase
    public MenuAdmin() {
    }

    // Constructor lengkap (Opsional, tapi berguna)
    public MenuAdmin(String menu_id, String menu_name, long price, String category, int stok, String discription) {
        this.menu_id = menu_id;
        this.menu_name = menu_name;
        this.price = price;
        this.category = category;
        this.stok = stok;
        this.discription = discription;
    }

    // --- GETTER & SETTER DENGAN ANOTASI AGAR COCOK DENGAN FIREBASE ---

    @PropertyName("menu_id")
    public String getMenu_id() { return menu_id; }
    @PropertyName("menu_id")
    public void setMenu_id(String menu_id) { this.menu_id = menu_id; }

    @PropertyName("menu_name")
    public String getMenu_name() { return menu_name; }
    @PropertyName("menu_name")
    public void setMenu_name(String menu_name) { this.menu_name = menu_name; }

    @PropertyName("price")
    public long getPrice() { return price; }
    @PropertyName("price")
    public void setPrice(long price) { this.price = price; }

    @PropertyName("category")
    public String getCategory() { return category; }
    @PropertyName("category")
    public void setCategory(String category) { this.category = category; }

    @PropertyName("stok")
    public int getStok() { return stok; }
    @PropertyName("stok")
    public void setStok(int stok) { this.stok = stok; }

    @PropertyName("discription") // Sesuaikan sama tulisan di ERD-mu
    public String getDiscription() { return discription; }
    @PropertyName("discription")
    public void setDiscription(String discription) { this.discription = discription; }

    @PropertyName("image_url")
    public String getImage_url() { return image_url; }
    @PropertyName("image_url")
    public void setImage_url(String image_url) { this.image_url = image_url; }

    @PropertyName("admin_id")
    public String getAdmin_id() { return admin_id; }
    @PropertyName("admin_id")
    public void setAdmin_id(String admin_id) { this.admin_id = admin_id; }
}
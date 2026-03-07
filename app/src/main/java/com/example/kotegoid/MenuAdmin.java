package com.example.kotegoid;

public class MenuAdmin {
    private String id;
    private String nama;
    private String harga;
    private String kategori;
    private String status;
    private String deskripsi;
    private String imageUrl;

    // Constructor kosong (diperlukan jika nanti pakai Firebase)
    public MenuAdmin() {
    }

    // Constructor lengkap
    public MenuAdmin(String id, String nama, String harga, String kategori, String status, String deskripsi, String imageUrl) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
        this.status = status;
        this.deskripsi = deskripsi;
        this.imageUrl = imageUrl;
    }

    // Getter dan Setter (Untuk mengambil dan mengisi data)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getHarga() { return harga; }
    public void setHarga(String harga) { this.harga = harga; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
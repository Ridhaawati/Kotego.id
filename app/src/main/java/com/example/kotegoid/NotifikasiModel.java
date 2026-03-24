package com.example.kotegoid;

public class NotifikasiModel {
    // Variabel ini harus sesuai dengan field di Firebase
    private String idPesanan;
    private String namaMenu;
    private String harga;
    private String kategori;
    private String status;
    private String imageUrl; // Tambahkan jika ada gambar

    // Constructor Kosong (Wajib ada untuk Firebase)
    public NotifikasiModel() {
    }

    // Constructor Lengkap
    public NotifikasiModel(String idPesanan, String namaMenu, String harga, String kategori, String status, String imageUrl) {
        this.idPesanan = idPesanan;
        this.namaMenu = namaMenu;
        this.harga = harga;
        this.kategori = kategori;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    // Getter dan Setter
    public String getIdPesanan() { return idPesanan; }
    public void setIdPesanan(String idPesanan) { this.idPesanan = idPesanan; }

    public String getNamaMenu() { return namaMenu; }
    public void setNamaMenu(String namaMenu) { this.namaMenu = namaMenu; }

    public String getHarga() { return harga; }
    public void setHarga(String harga) { this.harga = harga; }

    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
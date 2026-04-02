package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MenuManajemenAdmin extends AppCompatActivity {

    private RecyclerView rvMenu;
    private MenuAdapterAdmin adapter;
    private List<MenuAdmin> listMenu;
    private ImageView btnAdd, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_manajemen_admin);

        // 1. Inisialisasi View
        rvMenu = findViewById(R.id.rvMenu);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);

        // 2. Siapkan Data Dummy (SESUAI DENGAN MODEL MENUADMIN TERBARU)
        listMenu = new ArrayList<>();

        // Urutan sesuai MenuAdmin: ID, Nama, Harga (Angka), Kategori, Stok (Angka), Deskripsi
        listMenu.add(new MenuAdmin("1", "Ayam Chili Padi", 25000, "Makanan", 50, "Pedas nikmat"));
        listMenu.add(new MenuAdmin("2", "Es Teh Manis", 5000, "Minuman", 100, "Segar sekali"));

        // 3. Pasang Adapter ke RecyclerView
        adapter = new MenuAdapterAdmin(this, listMenu);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        rvMenu.setAdapter(adapter);

        // 4. Logika Tombol Tambah (+)
        btnAdd.setOnClickListener(v -> {
            // Nanti diaktifkan ke TambahMenuActivity
            Toast.makeText(this, "Membuka Tambah Menu...", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(MenuManajemenAdmin.this, TambahMenuActivity.class);
            // startActivity(intent);
        });

        // 5. Logika Tombol Back
        btnBack.setOnClickListener(v -> finish());
    }
}
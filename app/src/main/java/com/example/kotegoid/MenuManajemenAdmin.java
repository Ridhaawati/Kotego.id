package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

// Nama class sekarang sudah sama dengan nama file Anda
public class MenuManajemenAdmin extends AppCompatActivity {

    private RecyclerView rvMenu;
    private MenuAdapterAdmin adapter;
    private List<MenuAdmin> listMenu;
    private ImageView btnAdd, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Pastikan nama layout di bawah ini sesuai dengan nama file XML Anda
        setContentView(R.layout.activity_menu_manajemen_admin);

        // 1. Inisialisasi View
        rvMenu = findViewById(R.id.rvMenu);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);

        // 2. Siapkan Data Dummy (Data sementara untuk tes)
        listMenu = new ArrayList<>();
        listMenu.add(new MenuAdmin("1", "Ayam Chili Padi", "25.000", "Makanan", "On", "Pedas nikmat", ""));
        listMenu.add(new MenuAdmin("2", "Es Teh Manis", "5.000", "Minuman", "On", "Segar", ""));

        // 3. Pasang Adapter ke RecyclerView
        adapter = new MenuAdapterAdmin(this, listMenu);
        rvMenu.setLayoutManager(new LinearLayoutManager(this));
        rvMenu.setAdapter(adapter);

        // 4. Logika Tombol Tambah (+)
        btnAdd.setOnClickListener(v -> {
            // Nanti diaktifkan jika sudah buat Activity Tambah Menu
            // Intent intent = new Intent(MenuManajemenAdmin.this, TambahMenuActivity.class);
            // startActivity(intent);
        });

        // 5. Logika Tombol Back
        btnBack.setOnClickListener(v -> finish());
    }
}
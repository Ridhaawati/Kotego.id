package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profil extends AppCompatActivity {

    private ImageView btnBack, btnSettings, btnEdit;
    private LinearLayout layoutVoucher, layoutDiskon;
    private LinearLayout menuRiwayat, menuFavorit, menuBantuan;
    private Button btnKeluar;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Initialize views
        initViews();

        // Setup listeners
        setupListeners();

        // Setup bottom navigation
        setupBottomNavigation();

        // Handle back press with new API
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(Profil.this, MainActivity.class));
                finish();
            }
        });
    }

    private void initViews() {
        // Toolbar buttons
        btnBack = findViewById(R.id.btnBack);
        btnSettings = findViewById(R.id.btnSettings);
        btnEdit = findViewById(R.id.btnEdit);

        // Voucher & Diskon section
        layoutVoucher = findViewById(R.id.layoutVoucher);
        layoutDiskon = findViewById(R.id.layoutDiskon);

        // Menu items
        menuRiwayat = findViewById(R.id.menuRiwayat);
        menuFavorit = findViewById(R.id.menuFavorit);
        menuBantuan = findViewById(R.id.menuBantuan);

        // Keluar button
        btnKeluar = findViewById(R.id.btnKeluar);

        // Bottom navigation
        bottomNav = findViewById(R.id.bottomNav);
    }

    private void setupListeners() {
        // Back button
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(Profil.this, MainActivity.class));
            finish();
        });

        // Settings button
        btnSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Pengaturan", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(Profil.this, SettingsActivity.class);
            // startActivity(intent);
        });

        // Edit profile button
        btnEdit.setOnClickListener(v -> {
            Toast.makeText(this, "Edit Profil", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(Profil.this, EditProfilActivity.class);
            // startActivity(intent);
        });

        // Voucher section
        layoutVoucher.setOnClickListener(v -> {
            Toast.makeText(this, "Voucher", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(Profil.this, VoucherActivity.class);
            // startActivity(intent);
        });

        // Diskon section
        layoutDiskon.setOnClickListener(v -> {
            Toast.makeText(this, "Diskon", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(Profil.this, DiskonActivity.class);
            // startActivity(intent);
        });

        // Riwayat pesanan
        menuRiwayat.setOnClickListener(v -> {
            Toast.makeText(this, "Riwayat Pesanan", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(Profil.this, RiwayatPesananActivity.class);
            // startActivity(intent);
        });

        // Daftar Favorit
        menuFavorit.setOnClickListener(v -> {
            Toast.makeText(this, "Daftar Favorit", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(Profil.this, FavoritActivity.class);
            // startActivity(intent);
        });

        // Butuh Bantuan
        menuBantuan.setOnClickListener(v -> {
            Toast.makeText(this, "Butuh Bantuan", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(Profil.this, BantuanActivity.class);
            // startActivity(intent);
        });

        // Keluar button
        btnKeluar.setOnClickListener(v -> showLogoutDialog());
    }

    private void setupBottomNavigation() {
        // Set current menu as selected
        bottomNav.setSelectedItemId(R.id.nav_profil);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(Profil.this, MainActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_pesanan) {
                startActivity(new Intent(Profil.this, RiwayatPesanan.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_keranjang) {
                startActivity(new Intent(Profil.this, Keranjang.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_profil) {
                // Already on profile page
                return true;
            }

            return false;
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Keluar")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    // Clear user session/preferences here
                    // SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    // prefs.edit().clear().apply();

                    // Go to login page
                    Intent intent = new Intent(Profil.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                    Toast.makeText(this, "Berhasil keluar", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Batal", null)
                .show();
    }
}
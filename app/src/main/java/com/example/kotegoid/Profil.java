package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profil extends AppCompatActivity {

    private ImageView btnBack, btnSettings, btnEdit;
    private TextView tvNamaUser, tvEmailUser;
    private LinearLayout menuRiwayat;
    private Button btnKeluar;
    private BottomNavigationView bottomNav;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        mAuth = FirebaseAuth.getInstance();
        initViews();
        loadUserData();
        setupListeners();
        setupBottomNavigation();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(Profil.this, Home.class));
                finish();
            }
        });
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSettings = findViewById(R.id.btnSettings);
        btnEdit = findViewById(R.id.btnEdit);
        tvNamaUser = findViewById(R.id.tvNamaUser);
        tvEmailUser = findViewById(R.id.tvEmailUser);
        menuRiwayat = findViewById(R.id.menuRiwayat);
        btnKeluar = findViewById(R.id.btnKeluar);
        bottomNav = findViewById(R.id.bottomNav);
    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tvEmailUser.setText(user.getEmail());
            String name = user.getDisplayName();
            tvNamaUser.setText((name == null || name.isEmpty()) ? user.getEmail().split("@")[0] : name);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(Profil.this, Home.class));
            finish();
        });

        btnEdit.setOnClickListener(v -> startActivity(new Intent(this, EditProfile.class)));

        menuRiwayat.setOnClickListener(v -> startActivity(new Intent(this, RiwayatPesanan.class)));

        btnKeluar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Keluar")
                    .setMessage("Yakin ingin keluar?")
                    .setPositiveButton("Ya", (d, w) -> {
                        mAuth.signOut();
                        Intent intent = new Intent(this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Batal", null).show();
        });
    }

    private void setupBottomNavigation() {
        bottomNav.setSelectedItemId(R.id.nav_profil);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) startActivity(new Intent(this, Home.class));
            else if (id == R.id.nav_pesanan) startActivity(new Intent(this, RiwayatPesanan.class));
            else if (id == R.id.nav_keranjang) startActivity(new Intent(this, Keranjang.class));
            else return true;
            finish();
            return true;
        });
    }
}
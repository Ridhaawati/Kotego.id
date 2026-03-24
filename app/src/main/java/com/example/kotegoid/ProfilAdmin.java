package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfilAdmin extends AppCompatActivity {

    private Button btnLogout, btnEdit;
    private TextView tvNama, tvEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_admin);

        mAuth = FirebaseAuth.getInstance();

        tvNama = findViewById(R.id.tvNamaAdmin);
        tvEmail = findViewById(R.id.tvEmailAdmin);
        btnLogout = findViewById(R.id.btnLogout);
        btnEdit = findViewById(R.id.btnEditProfil);

        // Menampilkan data admin yang sedang login (Jika pakai Firebase Auth)
        if (mAuth.getCurrentUser() != null) {
            tvEmail.setText(mAuth.getCurrentUser().getEmail());
            // Nama bisa diambil dari database atau display name
        }

        // Fungsi Logout
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_SHORT).show();

            // Pindah ke halaman Login
            Intent intent = new Intent(ProfilAdmin.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnEdit.setOnClickListener(v -> {
            Toast.makeText(this, "Fitur Edit Profil segera hadir", Toast.LENGTH_SHORT).show();
        });
    }
}
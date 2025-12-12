package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText edtNama, edtEmail, edtPassword, edtConfirmPassword;
    Button btnDaftar;
    TextView btnPunyaAkun;

    // Firebase
    FirebaseAuth auth;
    DatabaseReference dbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // FIREBASE SETUP
        auth = FirebaseAuth.getInstance();
        dbUsers = FirebaseDatabase.getInstance().getReference("users");

        // Hubungkan XML
        edtNama = findViewById(R.id.edtNama);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnDaftar = findViewById(R.id.btnRegister);
        btnPunyaAkun = findViewById(R.id.text_login);

        btnDaftar.setOnClickListener(v -> registerUser());

        btnPunyaAkun.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
            finish();
        });
    }

    private void registerUser() {
        String nama = edtNama.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        // VALIDASI INPUT
        if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email tidak valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ========== BUAT AKUN DI FIREBASE AUTH ==========
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                // Ambil UID user
                String userId = auth.getCurrentUser().getUid();

                // Buat objek user untuk disimpan ke database
                UserData user = new UserData(nama, email, "customer");

                // Simpan ke Realtime Database berdasarkan UID
                dbUsers.child(userId).setValue(user).addOnCompleteListener(saveTask -> {

                    if (saveTask.isSuccessful()) {
                        Toast.makeText(Register.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class));
                        finish();
                    } else {
                        Toast.makeText(Register.this, "Gagal menyimpan data user!", Toast.LENGTH_SHORT).show();
                    }

                });

            } else {
                Toast.makeText(Register.this,
                        "Registrasi gagal: " + task.getException().getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
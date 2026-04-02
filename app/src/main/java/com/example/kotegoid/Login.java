package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtRegister;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edPhone);
        edtPassword = findViewById(R.id.edtpassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.tv_register);

        auth = FirebaseAuth.getInstance();

        // 1. TAMBAHKAN INI: Cek Sesi Login (Auto Login)
        if (auth.getCurrentUser() != null) {
            checkUserRole(auth.getCurrentUser().getUid());
        }

        txtRegister.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Register.class));
        });

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Email dan password wajib diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Login ke Firebase Auth
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                checkUserRole(user.getUid());
                            }
                        } else {
                            Toast.makeText(Login.this, "Login Gagal: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    // 2. PINDAHKAN LOGIKA ROLE KE METHOD SENDIRI (Biar Rapi)
    private void checkUserRole(String uid) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("users").child(uid);

        db.get().addOnCompleteListener(dataTask -> {
            if (dataTask.isSuccessful()) {
                DataSnapshot snapshot = dataTask.getResult();

                if (snapshot.exists()) {
                    String role = snapshot.child("role").getValue(String.class);

                    if ("admin".equals(role)) {
                        startActivity(new Intent(Login.this, HomeAdmin.class));
                        finish();
                    } else if ("customer".equals(role)) {
                        startActivity(new Intent(Login.this, Kategori.class));
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Role tidak dikenali!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Data user tidak ditemukan di Database!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Login.this, "Gagal mengambil data role!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
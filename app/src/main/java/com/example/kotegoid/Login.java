package com.example.kotegoid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Hubungkan dengan ID layout
        edtEmail = findViewById(R.id.edPhone);
        edtPassword = findViewById(R.id.edtpassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String emailInput = edtEmail.getText().toString().trim();
            String passInput = edtPassword.getText().toString().trim();

            // Validasi input
            if (emailInput.isEmpty() || passInput.isEmpty()) {
                Toast.makeText(Login.this, "Isi email dan password!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ambil data dari Firebase
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");

            db.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    boolean found = false;

                    // Loop semua user didalam node "users"
                    for (DataSnapshot userSnapshot : task.getResult().getChildren()) {

                        String dbEmail = userSnapshot.child("email").getValue(String.class);
                        String dbPassword = userSnapshot.child("password").getValue(String.class);
                        String role = userSnapshot.child("role").getValue(String.class);

                        // Cek kecocokan
                        if (emailInput.equals(dbEmail) && passInput.equals(dbPassword)) {
                            found = true;

                            Toast.makeText(Login.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();

                            // Arahkan berdasarkan role
                            if ("admin".equals(role)) {
                                startActivity(new Intent(Login.this, Home.class));
                            } else if ("customer".equals(role)) {
                                startActivity(new Intent(Login.this, Home.class));
                            } else {
                                Toast.makeText(Login.this, "Role tidak dikenal!", Toast.LENGTH_SHORT).show();
                            }

                            finish();
                            break;
                        }
                    }

                    if (!found) {
                        Toast.makeText(Login.this, "Email atau password salah!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Login.this, "Gagal mengambil data Firebase!", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
}

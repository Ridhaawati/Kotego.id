package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        // Click listener untuk pindah ke Register
        txtRegister.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        });

        // Click listener untuk login
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Email dan password harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Login Firebase Auth
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            FirebaseUser user = auth.getCurrentUser();
                            String uid = user.getUid();

                            // Ambil role dari Realtime Database
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("users").child(uid);

                            db.get().addOnCompleteListener(dataTask -> {
                                if (dataTask.isSuccessful()) {
                                    DataSnapshot snapshot = dataTask.getResult();

                                    String role = snapshot.child("role").getValue(String.class);

                                    if (role == null) {
                                        Toast.makeText(Login.this, "Role tidak ditemukan!", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    // Arahkan berdasarkan role
                                    if (role.equals("admin")) {
                                        startActivity(new Intent(Login.this, HomeAdmin.class));
                                    } else if (role.equals("customer")) {
                                        startActivity(new Intent(Login.this, Home.class));
                                    } else {
                                        Toast.makeText(Login.this, "Role tidak valid", Toast.LENGTH_SHORT).show();
                                    }

                                    finish();

                                } else {
                                    Toast.makeText(Login.this, "Gagal mengambil role!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(Login.this, "Email atau password salah!", Toast.LENGTH_SHORT).show();
                        }

                    });
        });
    }
}
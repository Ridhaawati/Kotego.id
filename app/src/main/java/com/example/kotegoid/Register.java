package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText edtNama, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnDaftar;
    private TextView btnPunyaAkun;

    private FirebaseAuth auth;
    private DatabaseReference dbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        dbUsers = FirebaseDatabase.getInstance().getReference("users");

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
        // 1. Ambil input dari user
        String inputNama = edtNama.getText().toString().trim();
        String inputEmail = edtEmail.getText().toString().trim();
        String inputPassword = edtPassword.getText().toString().trim();
        String inputConfirm = edtConfirmPassword.getText().toString().trim();

        // VALIDASI
        if (inputNama.isEmpty() || inputEmail.isEmpty() || inputPassword.isEmpty() || inputConfirm.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            Toast.makeText(this, "Email tidak valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inputPassword.length() < 6) {
            Toast.makeText(this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!inputPassword.equals(inputConfirm)) {
            Toast.makeText(this, "Konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnDaftar.setEnabled(false);

        // 2. Buat akun di Firebase Auth
        auth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful() && auth.getCurrentUser() != null) {
                String userId = auth.getCurrentUser().getUid();

                // 3. PERBAIKAN DI SINI: Sesuaikan dengan UserData(name, email, role)
                // Kita masukkan 'inputNama' ke parameter 'name'
                UserData user = new UserData(inputNama, inputEmail, "customer");

                dbUsers.child(userId).setValue(user).addOnCompleteListener(saveTask -> {
                    if (saveTask.isSuccessful()) {
                        Toast.makeText(Register.this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class));
                        finish();
                    } else {
                        btnDaftar.setEnabled(true);
                        Toast.makeText(Register.this, "Gagal simpan database!", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                btnDaftar.setEnabled(true);
                Toast.makeText(Register.this, "Gagal: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
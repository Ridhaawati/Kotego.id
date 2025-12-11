package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    EditText edtNama, edtEmail, edtPassword, edtConfirmPassword;
    Button btnDaftar;
    TextView btnPunyaAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Hubungkan ID XML
        edtNama = findViewById(R.id.edtNama);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnDaftar = findViewById(R.id.btnRegister);
        btnPunyaAkun = findViewById(R.id.text_login); // ini TextView

        // Tombol daftar ditekan
        btnDaftar.setOnClickListener(v -> {
            String nama = edtNama.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            // Validasi
            if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(Register.this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(Register.this, "Password tidak sama!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Koneksi Firebase
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");

            // Buat ID unik
            String userId = db.push().getKey();

            // Model user
            UserData user = new UserData(nama, email, "customer");

            // Simpan ke Firebase
            assert userId != null;
            db.child(userId).setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this, Login.class));
                    finish();
                } else {
                    Toast.makeText(Register.this, "Gagal menyimpan data!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Tombol "Masuk"
        btnPunyaAkun.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
            finish();
        });
    }
}

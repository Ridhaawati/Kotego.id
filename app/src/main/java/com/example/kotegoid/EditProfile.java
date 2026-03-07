package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfile extends AppCompatActivity {

    private ImageView btnBack, ivProfilePicture;
    private TextView btnUbahFoto;
    private EditText etNamaLengkap, etUsername, etSandi, etKonfirmasiSandi, etEmail;
    private Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        initViews();

        // Load current user data
        loadUserData();

        // Setup listeners
        setupListeners();

        // Handle back press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        btnUbahFoto = findViewById(R.id.btnUbahFoto);
        etNamaLengkap = findViewById(R.id.etNamaLengkap);
        etUsername = findViewById(R.id.etUsername);
        etSandi = findViewById(R.id.etSandi);
        etKonfirmasiSandi = findViewById(R.id.etKonfirmasiSandi);
        etEmail = findViewById(R.id.etEmail);
        btnSimpan = findViewById(R.id.btnSimpan);
    }

    private void loadUserData() {
        // TODO: Load user data from SharedPreferences or Database
        // For now, we'll use placeholder data
        etNamaLengkap.setText("Ridhawati");
        etUsername.setText("ridhawati123");
        etEmail.setText("idha05@gmail.com");

        // Don't show password for security
        etSandi.setText("");
        etKonfirmasiSandi.setText("");
    }

    private void setupListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Ubah foto button
        btnUbahFoto.setOnClickListener(v -> {
            Toast.makeText(this, "Fitur ubah foto sedang dalam pengembangan", Toast.LENGTH_SHORT).show();
            // TODO: Implement image picker
            // Intent intent = new Intent(Intent.ACTION_PICK);
            // intent.setType("image/*");
            // startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        // Simpan button
        btnSimpan.setOnClickListener(v -> saveProfile());
    }

    private void saveProfile() {
        // Get input values
        String namaLengkap = etNamaLengkap.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String sandi = etSandi.getText().toString().trim();
        String konfirmasiSandi = etKonfirmasiSandi.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        // Validation
        if (namaLengkap.isEmpty()) {
            etNamaLengkap.setError("Nama lengkap tidak boleh kosong");
            etNamaLengkap.requestFocus();
            return;
        }

        if (username.isEmpty()) {
            etUsername.setError("Username tidak boleh kosong");
            etUsername.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email tidak boleh kosong");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Format email tidak valid");
            etEmail.requestFocus();
            return;
        }

        // Validate password if user wants to change it
        if (!sandi.isEmpty() || !konfirmasiSandi.isEmpty()) {
            if (sandi.length() < 6) {
                etSandi.setError("Sandi minimal 6 karakter");
                etSandi.requestFocus();
                return;
            }

            if (!sandi.equals(konfirmasiSandi)) {
                etKonfirmasiSandi.setError("Konfirmasi sandi tidak cocok");
                etKonfirmasiSandi.requestFocus();
                return;
            }
        }

        // TODO: Save to SharedPreferences or Database
        // SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        // SharedPreferences.Editor editor = prefs.edit();
        // editor.putString("nama_lengkap", namaLengkap);
        // editor.putString("username", username);
        // editor.putString("email", email);
        // if (!sandi.isEmpty()) {
        //     editor.putString("password", sandi); // In real app, hash the password!
        // }
        // editor.apply();

        Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
        finish();
    }
}
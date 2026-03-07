package com.example.kotegoid;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TambahMenu extends AppCompatActivity {

    // Deklarasi variabel
    private EditText etNama, etHarga, etKategori, etStatus, etDeskripsi;
    private TextView tvTitle;
    private ImageView btnBack;
    private Button btnSimpan;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_menu);

        // 1. Inisialisasi View (Pastikan ID di XML activity_tambah_menu sama)
        etNama = findViewById(R.id.etNama);
        etHarga = findViewById(R.id.etHarga);
        etKategori = findViewById(R.id.etKategori);
        etStatus = findViewById(R.id.etStatus);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        tvTitle = findViewById(R.id.tvTitlePage); // TextView di header
        btnBack = findViewById(R.id.btnBack);
        btnSimpan = findViewById(R.id.btnSimpan);

        // 2. Cek apakah ini mode EDIT atau TAMBAH
        // Jika ada data yang dikirim dari Adapter, berarti EDIT
        if (getIntent().hasExtra("nama")) {
            isEditMode = true;
            tvTitle.setText("Edit Menu"); // Ubah judul header

            // Set data lama ke EditText agar bisa diedit
            etNama.setText(getIntent().getStringExtra("nama"));
            etHarga.setText(getIntent().getStringExtra("harga"));
            etKategori.setText(getIntent().getStringExtra("kategori"));
            etStatus.setText(getIntent().getStringExtra("status"));
            // (Tambahkan deskripsi jika dikirim juga)
        } else {
            isEditMode = false;
            tvTitle.setText("Tambah Menu");
        }

        // 3. Tombol Kembali
        btnBack.setOnClickListener(v -> finish());

        // 4. Tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            simpanData();
        });
    }

    private void simpanData() {
        String nama = etNama.getText().toString();

        if (nama.isEmpty()) {
            Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {
            // Logika untuk UPDATE data ke database
            Toast.makeText(this, "Berhasil memperbarui " + nama, Toast.LENGTH_SHORT).show();
        } else {
            // Logika untuk SIMPAN data baru ke database
            Toast.makeText(this, "Berhasil menambahkan " + nama, Toast.LENGTH_SHORT).show();
        }

        finish(); // Tutup halaman setelah simpan
    }
}
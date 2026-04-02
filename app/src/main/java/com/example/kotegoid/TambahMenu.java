package com.example.kotegoid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class TambahMenu extends AppCompatActivity {

    private EditText etNama, etHarga, etKategori, etStatus, etDeskripsi;
    private TextView tvTitle;
    private ImageView btnBack, imgMenuPreview;
    private Button btnSimpan;
    private com.google.android.material.floatingactionbutton.FloatingActionButton btnPickImage;

    private boolean isEditMode = false;
    private String menuId;

    // Firebase variables
    private DatabaseReference dbRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_menu);

        // 1. Inisialisasi View sesuai XML kamu
        etNama = findViewById(R.id.etNama);
        etHarga = findViewById(R.id.etHarga);
        etKategori = findViewById(R.id.etKategori);
        etStatus = findViewById(R.id.etStatus);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        tvTitle = findViewById(R.id.tvTitlePage);
        btnBack = findViewById(R.id.btnBack);
        btnSimpan = findViewById(R.id.btnSimpan);
        imgMenuPreview = findViewById(R.id.imgMenuPreview);
        btnPickImage = findViewById(R.id.btnPickImage);

        // 2. Inisialisasi Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("menu");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("gambar_menu");

        // 3. Logika Edit
        if (getIntent().hasExtra("menu_id")) {
            isEditMode = true;
            menuId = getIntent().getStringExtra("menu_id");
            tvTitle.setText("Edit Menu");
            etNama.setText(getIntent().getStringExtra("menu_name"));
            etHarga.setText(String.valueOf(getIntent().getLongExtra("price", 0)));
            etKategori.setText(getIntent().getStringExtra("category"));
            etStatus.setText(getIntent().getStringExtra("status"));
            etDeskripsi.setText(getIntent().getStringExtra("discription"));
            // (Nanti bisa tambahkan Glide untuk load gambar lama di sini)
        }

        // 4. Pilih Gambar
        btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 100);
        });

        btnBack.setOnClickListener(v -> finish());
        btnSimpan.setOnClickListener(v -> simpanData());
    }

    // Menangkap hasil dari Galeri
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imgMenuPreview.setImageURI(imageUri);
        }
    }

    private void simpanData() {
        String nama = etNama.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();
        String kategori = etKategori.getText().toString().trim();
        String status = etStatus.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();

        if (nama.isEmpty() || hargaStr.isEmpty()) {
            Toast.makeText(this, "Nama dan Harga wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        long harga = Long.parseLong(hargaStr);
        if (!isEditMode) {
            menuId = dbRef.push().getKey();
        }

        // Jika ada gambar baru yang dipilih, upload dulu
        if (imageUri != null) {
            uploadGambarDanSimpan(nama, harga, kategori, status, deskripsi);
        } else {
            // Jika tidak pilih gambar, simpan teks saja
            simpanTeksKeDatabase(nama, harga, kategori, status, deskripsi, "");
        }
    }

    private void uploadGambarDanSimpan(String nama, long harga, String kategori, String status, String deskripsi) {
        StorageReference fileRef = storageRef.child(menuId + ".jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String urlGambar = uri.toString();
                simpanTeksKeDatabase(nama, harga, kategori, status, deskripsi, urlGambar);
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Gagal upload gambar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void simpanTeksKeDatabase(String nama, long harga, String kategori, String status, String deskripsi, String urlGambar) {
        MenuAdmin menu = new MenuAdmin(menuId, nama, harga, kategori, 50, deskripsi);
        if (!urlGambar.isEmpty()) {
            menu.setImage_url(urlGambar);
        }

        dbRef.child(menuId).setValue(menu).addOnSuccessListener(unused -> {
            dbRef.child(menuId).child("status").setValue(status);
            Toast.makeText(TambahMenu.this, "Berhasil disimpan!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
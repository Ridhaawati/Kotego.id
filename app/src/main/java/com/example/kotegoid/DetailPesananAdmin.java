package com.example.kotegoid;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailPesananAdmin extends AppCompatActivity {

    private String idPesanan;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan_admin);

        // 1. Ambil Data dari Intent (Pastikan dikirim dari daftar pesanan admin)
        idPesanan = getIntent().getStringExtra("idPesanan");
        String namaMenu = getIntent().getStringExtra("namaMenu");
        String namaPembeli = getIntent().getStringExtra("namaPembeli");
        String harga = getIntent().getStringExtra("harga");
        String img = getIntent().getStringExtra("image");

        // 2. Inisialisasi View
        TextView tvNamaMenu = findViewById(R.id.tvNamaMenu);
        TextView tvNamaPembeli = findViewById(R.id.tvNamaPembeli);
        TextView tvTotal = findViewById(R.id.tvTotalBayar);
        ImageView imgMenu = findViewById(R.id.imgMenu);
        Button btnSelesai = findViewById(R.id.btnSelesai);

        // 3. Tampilkan Data
        tvNamaMenu.setText(namaMenu);
        tvNamaPembeli.setText("Nama : " + namaPembeli);
        tvTotal.setText(harga);

        Glide.with(this)
                .load(img)
                .placeholder(R.drawable.miepedas) // Gunakan placeholder yang sudah ada
                .into(imgMenu);

        // 4. Update Status ke Firebase (FOLDER: orders)
        btnSelesai.setOnClickListener(v -> {
            if (idPesanan != null) {
                // Pastikan folder ini sama dengan folder saat Checkout, yaitu "orders"
                dbRef = FirebaseDatabase.getInstance().getReference("orders").child(idPesanan);

                dbRef.child("status").setValue("Selesai").addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Pesanan Berhasil Diselesaikan!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Gagal mengupdate status", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "ID Pesanan tidak valid", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}
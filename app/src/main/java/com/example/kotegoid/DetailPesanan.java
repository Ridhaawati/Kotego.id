package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailPesanan extends AppCompatActivity {

    ImageView btnBack, btnHelp;
    TextView txtNamaPemesan, txtTanggalPesanan;
    RecyclerView rvItemPesanan;
    Button btnLihatStruk;

    private String orderId;
    private List<CartItem> items; // Pakai CartItem agar sinkron dengan database
    private DetailPesananAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        // 1. Inisialisasi views
        btnBack = findViewById(R.id.btnBack);
        btnHelp = findViewById(R.id.btnHelp);
        txtNamaPemesan = findViewById(R.id.txtNamaPemesan);
        txtTanggalPesanan = findViewById(R.id.txtTanggalPesanan);
        rvItemPesanan = findViewById(R.id.rvItemPesanan);
        btnLihatStruk = findViewById(R.id.btnLihatStruk);

        // 2. Ambil Order ID dari Intent (Dikirim dari halaman Riwayat/Notif)
        orderId = getIntent().getStringExtra("ORDER_ID");

        if (orderId != null) {
            loadDetailPesanan(orderId);
        } else {
            Toast.makeText(this, "ID Pesanan tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 3. Button listeners
        btnBack.setOnClickListener(v -> finish());

        btnLihatStruk.setOnClickListener(v -> {
            // Berpindah ke BuktiPesanan dengan membawa ID yang sama
            Intent intent = new Intent(DetailPesanan.this, BuktiPesanan.class);
            intent.putExtra("ORDER_ID", orderId);
            startActivity(intent);
        });
    }

    private void loadDetailPesanan(String id) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("orders").child(id);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Set Nama & Tanggal dari Firebase
                    String userId = snapshot.child("user_id").getValue(String.class);
                    long timestamp = snapshot.child("timestamp").getValue(Long.class);

                    txtNamaPemesan.setText(userId);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, HH:mm", new Locale("id", "ID"));
                    txtTanggalPesanan.setText(sdf.format(new Date(timestamp)));

                    // Ambil list item pesanan
                    items = new ArrayList<>();
                    for (DataSnapshot itemSnap : snapshot.child("items").getChildren()) {
                        CartItem item = itemSnap.getValue(CartItem.class);
                        items.add(item);
                    }

                    // 4. Setup RecyclerView dengan data asli
                    // Pastikan DetailPesananAdapter kamu sudah diupdate untuk menerima List<CartItem>
                    adapter = new DetailPesananAdapter(items);
                    rvItemPesanan.setLayoutManager(new LinearLayoutManager(DetailPesanan.this));
                    rvItemPesanan.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailPesanan.this, "Gagal memuat detail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
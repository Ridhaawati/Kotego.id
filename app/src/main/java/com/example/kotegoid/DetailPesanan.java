package com.example.kotegoid;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DetailPesanan extends AppCompatActivity {

    ImageView btnBack, btnHelp;
    TextView txtNamaPemesan, txtTanggalPesanan;
    RecyclerView rvItemPesanan;
    Button btnLihatStruk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        btnHelp = findViewById(R.id.btnHelp);
        txtNamaPemesan = findViewById(R.id.txtNamaPemesan);
        txtTanggalPesanan = findViewById(R.id.txtTanggalPesanan);
        rvItemPesanan = findViewById(R.id.rvItemPesanan);
        btnLihatStruk = findViewById(R.id.btnLihatStruk);

        // Button listeners
        btnBack.setOnClickListener(v -> finish());
        btnHelp.setOnClickListener(v -> {
            // Buka help/FAQ
        });

        // Dummy data (nanti diganti dengan data dari Firebase)
        List<OrderItemModel> items = new ArrayList<>();
        items.add(new OrderItemModel("Ayam Chili Padi", 1, 25000, ""));
        items.add(new OrderItemModel("Ayam Chili Padi", 1, 25000, ""));

        // Setup RecyclerView
        DetailPesananAdapter adapter = new DetailPesananAdapter(items);
        rvItemPesanan.setLayoutManager(new LinearLayoutManager(this));
        rvItemPesanan.setAdapter(adapter);

        btnLihatStruk.setOnClickListener(v -> {
            // Buka activity Struk
        });
    }
}
package com.example.kotegoid;

import android.os.Bundle;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

public class Notifikasi extends AppCompatActivity {

    private RecyclerView rvNotifikasi;
    private NotifikasiAdapter adapter;
    private List<NotifikasiModel> listNotif;
    private DatabaseReference dbRef;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        // 1. Inisialisasi View
        btnBack = findViewById(R.id.btn_back_notif);
        rvNotifikasi = findViewById(R.id.rv_notifikasi);

        // Setting RecyclerView agar rapi
        rvNotifikasi.setLayoutManager(new LinearLayoutManager(this));
        rvNotifikasi.setHasFixedSize(true);

        // 2. Siapkan List dan Adapter
        listNotif = new ArrayList<>();
        adapter = new NotifikasiAdapter(this, listNotif);
        rvNotifikasi.setAdapter(adapter);

        // 3. Hubungkan ke Firebase (Tabel "Pesanan")
        dbRef = FirebaseDatabase.getInstance().getReference("Pesanan");

        // 4. FUNGSI OTOMATIS: Ambil data secara Realtime
        ambilDataPesanan();

        // 5. Tombol Kembali
        btnBack.setOnClickListener(v -> finish());
    }

    private void ambilDataPesanan() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Bersihkan list lama supaya tidak double saat ada data baru
                listNotif.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NotifikasiModel notif = data.getValue(NotifikasiModel.class);
                        listNotif.add(notif);
                    }
                    // Beritahu adapter kalau ada data baru masuk
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Notifikasi.this, "Gagal mengambil data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
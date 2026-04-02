package com.example.kotegoid;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RiwayatLaporan extends AppCompatActivity {

    private RecyclerView rvRiwayat;
    private NotifikasiAdapter adapter;
    private List<NotifikasiModel> listSelesai;
    private TextView tvTotalLaporan;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_laporan);

        tvTotalLaporan = findViewById(R.id.tvTotalLaporan);
        rvRiwayat = findViewById(R.id.rvRiwayatSelesai);
        rvRiwayat.setLayoutManager(new LinearLayoutManager(this));

        listSelesai = new ArrayList<>();
        adapter = new NotifikasiAdapter(this, listSelesai);
        rvRiwayat.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("Pesanan");

        // Ambil data pesanan yang sudah selesai
        dbRef.orderByChild("status").equalTo("Selesai")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listSelesai.clear();
                        long totalUang = 0;

                        for (DataSnapshot data : snapshot.getChildren()) {
                            NotifikasiModel model = data.getValue(NotifikasiModel.class);
                            if (model != null) {
                                listSelesai.add(model);

                                // Hitung total pendapatan
                                try {
                                    // Mengambil string harga, hapus karakter selain angka
                                    String hargaStr = model.getHarga();
                                    if (hargaStr != null) {
                                        String nominal = hargaStr.replaceAll("[^0-9]", "");
                                        if (!nominal.isEmpty()) {
                                            totalUang += Long.parseLong(nominal);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();

                        // Format ke Rupiah: titik sebagai pemisah ribuan
                        String formattedTotal = String.format(Locale.GERMANY, "%,d", totalUang);
                        tvTotalLaporan.setText("Rp " + formattedTotal);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

        if (findViewById(R.id.btnBackRiwayat) != null) {
            findViewById(R.id.btnBackRiwayat).setOnClickListener(v -> finish());
        }
    }
}
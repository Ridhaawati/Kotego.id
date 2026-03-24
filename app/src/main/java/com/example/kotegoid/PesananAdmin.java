package com.example.kotegoid;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class PesananAdmin extends AppCompatActivity {

    private RecyclerView rvPesanan;
    private List<NotifikasiModel> listPesanan;
    private NotifikasiAdapter adapter; // Kita pakai adapter yang mirip dengan notif
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_admin);

        rvPesanan = findViewById(R.id.rvDaftarPesanan);
        rvPesanan.setLayoutManager(new LinearLayoutManager(this));

        listPesanan = new ArrayList<>();
        adapter = new NotifikasiAdapter(this, listPesanan);
        rvPesanan.setAdapter(adapter);

        // Ambil data dari Firebase tabel "Pesanan"
        dbRef = FirebaseDatabase.getInstance().getReference("Pesanan");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPesanan.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    NotifikasiModel model = data.getValue(NotifikasiModel.class);
                    // Pastikan ID dari Firebase tersimpan ke Model
                    if (model != null) {
                        model.setIdPesanan(data.getKey());
                        listPesanan.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
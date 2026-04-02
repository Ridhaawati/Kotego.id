package com.example.kotegoid;

import android.os.Bundle;
import android.widget.Toast;
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
    private NotifikasiAdapter adapter;
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

        // Ambil data dari folder "orders" (Bukan "Pesanan") agar sinkron
        dbRef = FirebaseDatabase.getInstance().getReference("orders");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPesanan.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    NotifikasiModel model = data.getValue(NotifikasiModel.class);
                    if (model != null) {
                        model.setIdPesanan(data.getKey());
                        listPesanan.add(model);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PesananAdmin.this, "Gagal memuat data!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
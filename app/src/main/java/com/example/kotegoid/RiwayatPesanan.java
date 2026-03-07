package com.example.kotegoid;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
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

public class RiwayatPesanan extends AppCompatActivity {

    ImageView btnBack;
    RecyclerView rvRiwayat;
    LinearLayout layoutEmpty;

    DatabaseReference db;
    FirebaseAuth auth;

    List<RiwayatPesananModel> riwayatList;
    RiwayatPesananAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_pesanan);

        db = FirebaseDatabase.getInstance().getReference("orders");
        auth = FirebaseAuth.getInstance();

        btnBack = findViewById(R.id.btnBack);
        rvRiwayat = findViewById(R.id.rvRiwayat);
        layoutEmpty = findViewById(R.id.layoutEmpty);

        btnBack.setOnClickListener(v -> finish());

        riwayatList = new ArrayList<>();
        adapter = new RiwayatPesananAdapter(riwayatList);

        rvRiwayat.setLayoutManager(new LinearLayoutManager(this));
        rvRiwayat.setAdapter(adapter);

        loadRiwayatPesanan();
    }

    private void loadRiwayatPesanan() {
        String userId = auth.getUid();

        db.orderByChild("user_id").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        riwayatList.clear();

                        for (DataSnapshot data : snapshot.getChildren()) {
                            String orderId = data.getKey();
                            String orderDate = data.child("order_date").getValue(String.class);
                            String status = data.child("order_status").getValue(String.class);
                            Integer totalPrice = data.child("total_price").getValue(Integer.class);
                            Long timestamp = data.child("timestamp").getValue(Long.class);

                            // Parse tanggal
                            String tanggal = "N/A";
                            String waktu = "N/A";

                            if (orderDate != null) {
                                String[] parts = orderDate.split(" ");
                                if (parts.length >= 2) {
                                    tanggal = parts[0]; // "12/12/2025"
                                    waktu = parts[1];   // "14:05:30"
                                }
                            }

                            RiwayatPesananModel model = new RiwayatPesananModel(
                                    orderId,
                                    tanggal,
                                    waktu,
                                    status != null ? status : "Pending",
                                    totalPrice != null ? totalPrice : 0,
                                    timestamp != null ? timestamp : 0
                            );

                            riwayatList.add(model);
                        }

                        // Tampilkan empty state jika tidak ada data
                        if (riwayatList.isEmpty()) {
                            layoutEmpty.setVisibility(View.VISIBLE);
                            rvRiwayat.setVisibility(View.GONE);
                        } else {
                            layoutEmpty.setVisibility(View.GONE);
                            rvRiwayat.setVisibility(View.VISIBLE);
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        layoutEmpty.setVisibility(View.VISIBLE);
                        rvRiwayat.setVisibility(View.GONE);
                    }
                });
    }
}
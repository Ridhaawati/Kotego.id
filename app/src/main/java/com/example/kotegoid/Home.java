package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerViewMenu;
    private MenuAdapter menuAdapter;
    private List<MenuAdmin> menuList; // Pakai MenuAdmin agar sinkron dengan database
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 1. Inisialisasi RecyclerView
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu);
        recyclerViewMenu.setLayoutManager(new GridLayoutManager(this, 2));

        menuList = new ArrayList<>();
        // Pastikan MenuAdapter kamu sudah diupdate untuk menerima Context dan List<MenuAdmin>
        menuAdapter = new MenuAdapter(this, menuList);
        recyclerViewMenu.setAdapter(menuAdapter);

        // 2. Ambil Data Realtime dari Firebase
        dbRef = FirebaseDatabase.getInstance().getReference("menu");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menuList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    MenuAdmin menu = data.getValue(MenuAdmin.class);
                    menuList.add(menu);
                }
                menuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this, "Gagal memuat menu", Toast.LENGTH_SHORT).show();
            }
        });

        // 3. Navigasi Bottom Menu (Tetap sama)
        LinearLayout navKeranjang = findViewById(R.id.nav_keranjang);
        navKeranjang.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Keranjang.class);
            startActivity(intent);
        });

        // Tambahkan navigasi profil jika perlu
        findViewById(R.id.nav_profil).setOnClickListener(v -> {
            startActivity(new Intent(Home.this, Profil.class));
        });
    }
}
package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerViewMenu;
    private MenuAdapter menuAdapter;
    private List<MenuItem> menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inisialisasi RecyclerView
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu);
        recyclerViewMenu.setLayoutManager(new GridLayoutManager(this, 2)); // 2 kolom

        // Buat data menu
        menuList = new ArrayList<>();
        menuList.add(new MenuItem("Beef Cah Cabe Ijo", R.drawable.miepedas));
        menuList.add(new MenuItem("Ayam Chili Padi", R.drawable.miepedas));
        menuList.add(new MenuItem("Udang Sambal Bakar", R.drawable.miepedas));
        menuList.add(new MenuItem("Ayam Geprek", R.drawable.miepedas));
        menuList.add(new MenuItem("Menu Lanjutan 1", R.drawable.miepedas));
        menuList.add(new MenuItem("Menu Lanjutan 2", R.drawable.miepedas));

        // Set adapter
        menuAdapter = new MenuAdapter(menuList);
        recyclerViewMenu.setAdapter(menuAdapter);

        // Navigasi ke Keranjang
        LinearLayout navKeranjang = findViewById(R.id.nav_keranjang);
        navKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Keranjang.class);
                startActivity(intent);
            }
        });
    }
}
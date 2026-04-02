package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProductDescription extends AppCompatActivity {

    private ImageView detailMenuImage, btnBack, btnLove;
    private TextView detailMenuName, detailMenuPrice, detailMenuDescription;
    private Button btnOrder;

    // Variabel untuk menampung data dari Intent
    private String menuId, menuName, menuDesc, menuImageUrl;
    private long menuPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        // 1. Inisialisasi views
        detailMenuImage = findViewById(R.id.detailMenuImage);
        btnBack = findViewById(R.id.btnBack);
        btnLove = findViewById(R.id.btnLove);
        detailMenuName = findViewById(R.id.detailMenuName);
        detailMenuPrice = findViewById(R.id.detailMenuPrice);
        detailMenuDescription = findViewById(R.id.detailMenuDescription);
        btnOrder = findViewById(R.id.btnOrder);

        // 2. Ambil data dari Intent (Kirim dari MenuAdapter)
        menuId = getIntent().getStringExtra("menu_id");
        menuName = getIntent().getStringExtra("menu_name");
        menuPrice = getIntent().getLongExtra("price", 0);
        menuDesc = getIntent().getStringExtra("description");
        menuImageUrl = getIntent().getStringExtra("image_url");

        // 3. Set data ke view secara dinamis
        detailMenuName.setText(menuName);
        detailMenuPrice.setText("Rp " + String.valueOf(menuPrice));
        detailMenuDescription.setText(menuDesc);

        // Load gambar dari URL Firebase menggunakan Glide
        Glide.with(this)
                .load(menuImageUrl)
                .placeholder(R.drawable.miepedas)
                .into(detailMenuImage);

        // Tombol Back
        btnBack.setOnClickListener(v -> finish());

        // Tombol Love
        btnLove.setOnClickListener(v -> Toast.makeText(ProductDescription.this, "Ditambahkan ke favorit", Toast.LENGTH_SHORT).show());

        // Tombol Pesan Sekarang - Tampilkan Bottom Sheet
        btnOrder.setOnClickListener(v -> showBottomSheet());
    }

    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_order, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Inisialisasi views di bottom sheet
        ImageView sheetMenuImage = bottomSheetView.findViewById(R.id.sheetMenuImage);
        TextView sheetMenuPrice = bottomSheetView.findViewById(R.id.sheetMenuPrice);
        TextView txtQuantity = bottomSheetView.findViewById(R.id.txtQuantity);
        TextView btnMinus = bottomSheetView.findViewById(R.id.btnMinus);
        TextView btnPlus = bottomSheetView.findViewById(R.id.btnPlus);
        Button btnPesanSekarang = bottomSheetView.findViewById(R.id.btnPesanSekarang);
        Button btnCekKeranjang = bottomSheetView.findViewById(R.id.btnCekKeranjang);
        ImageView btnCloseSheet = bottomSheetView.findViewById(R.id.btnCloseSheet);

        // Set data dari Firebase ke Bottom Sheet
        sheetMenuPrice.setText("Rp " + String.valueOf(menuPrice));
        Glide.with(this).load(menuImageUrl).into(sheetMenuImage);

        final int[] quantity = {1};

        // Logika Tambah Kurang Quantity
        btnPlus.setOnClickListener(v -> {
            quantity[0]++;
            txtQuantity.setText(String.valueOf(quantity[0]));
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                txtQuantity.setText(String.valueOf(quantity[0]));
            }
        });

        btnCloseSheet.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // TOMBOL SIMPAN KE FIREBASE (CART)
        btnPesanSekarang.setOnClickListener(v -> {
            // Gunakan ID user sementara (Misal: user1)
            String userId = "user1";
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);

            // Data yang akan disimpan
            HashMap<String, Object> cartItem = new HashMap<>();
            cartItem.put("menu_id", menuId);
            cartItem.put("menu_name", menuName);
            cartItem.put("price", menuPrice);
            cartItem.put("quantity", quantity[0]);
            cartItem.put("total_price", (menuPrice * quantity[0]));
            cartItem.put("image_url", menuImageUrl);

            // Simpan ke Realtime Database
            cartRef.child(menuId).setValue(cartItem).addOnSuccessListener(unused -> {
                Toast.makeText(ProductDescription.this, "Berhasil masuk keranjang!", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }).addOnFailureListener(e -> {
                Toast.makeText(ProductDescription.this, "Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

        btnCekKeranjang.setOnClickListener(v -> {
            startActivity(new Intent(ProductDescription.this, Keranjang.class));
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}
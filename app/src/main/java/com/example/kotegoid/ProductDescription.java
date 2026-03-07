package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ProductDescription extends AppCompatActivity {

    private ImageView detailMenuImage, btnBack, btnLove;
    private TextView detailMenuName, detailMenuPrice, detailMenuDescription;
    private Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        // Inisialisasi views
        detailMenuImage = findViewById(R.id.detailMenuImage);
        btnBack = findViewById(R.id.btnBack);
        btnLove = findViewById(R.id.btnLove);
        detailMenuName = findViewById(R.id.detailMenuName);
        detailMenuPrice = findViewById(R.id.detailMenuPrice);
        detailMenuDescription = findViewById(R.id.detailMenuDescription);
        btnOrder = findViewById(R.id.btnOrder);

        // Ambil data dari Intent
        String menuName = getIntent().getStringExtra("menuName");
        int menuImage = getIntent().getIntExtra("menuImage", R.drawable.miepedas);

        // Set data ke view
        detailMenuName.setText(menuName);
        detailMenuImage.setImageResource(menuImage);
        detailMenuPrice.setText("Rp. 25.000");
        detailMenuDescription.setText("Deskripsi lengkap untuk " + menuName + ". Hidangan lezat dengan bumbu pilihan yang menggugah selera.");

        // Tombol Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Tombol Love
        btnLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductDescription.this, "Ditambahkan ke favorit", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol Pesan Sekarang - Tampilkan Bottom Sheet
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(menuName, menuImage);
            }
        });
    }

    private void showBottomSheet(String menuName, int menuImage) {
        // Buat Bottom Sheet Dialog
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

        // Set data
        sheetMenuImage.setImageResource(menuImage);
        sheetMenuPrice.setText("Rp. 25.000");

        final int[] quantity = {1}; // Array untuk menyimpan jumlah

        // Tombol Plus
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity[0]++;
                txtQuantity.setText(String.valueOf(quantity[0]));
            }
        });

        // Tombol Minus
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity[0] > 1) {
                    quantity[0]--;
                    txtQuantity.setText(String.valueOf(quantity[0]));
                }
            }
        });

        // Tombol Close
        btnCloseSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        // Tombol Pesan Sekarang
        btnPesanSekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductDescription.this, "Pesanan " + menuName + " x" + quantity[0] + " berhasil!", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });

        // Tombol Cek Keranjang - Navigasi ke Keranjang
        btnCekKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDescription.this, Keranjang.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });

        // Tampilkan bottom sheet
        bottomSheetDialog.show();
    }
}
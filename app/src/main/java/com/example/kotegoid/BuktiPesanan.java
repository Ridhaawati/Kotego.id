package com.example.kotegoid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BuktiPesanan extends AppCompatActivity {

    ImageView btnClose;
    TextView txtTanggalWaktu, txtStatus, txtKasir;
    TextView txtTotalItem, txtSubtotal, txtTunai, txtKembalian;
    RecyclerView rvItems;
    LinearLayout btnShare, btnDownload, strukContainer;

    List<CartItem> itemList; // Menggunakan model CartItem yang sudah ada
    CartAdapter adapter; // Menggunakan CartAdapter untuk menampilkan item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bukti_pesanan);

        // 1. Inisialisasi views
        initViews();

        // 2. Ambil ORDER_ID dari Intent (dikirim dari Keranjang.java)
        String orderId = getIntent().getStringExtra("ORDER_ID");

        if (orderId != null) {
            // Ambil data asli dari Firebase
            loadOrderData(orderId);
        } else {
            Toast.makeText(this, "ID Pesanan tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 3. Button listeners
        btnClose.setOnClickListener(v -> finish());
        btnShare.setOnClickListener(v -> shareStruk());
        btnDownload.setOnClickListener(v -> downloadStruk());
    }

    private void initViews() {
        btnClose = findViewById(R.id.btnClose);
        txtTanggalWaktu = findViewById(R.id.txtTanggalWaktu);
        txtStatus = findViewById(R.id.txtStatus);
        txtKasir = findViewById(R.id.txtKasir);
        txtTotalItem = findViewById(R.id.txtTotalItem);
        txtSubtotal = findViewById(R.id.txtSubtotal);
        txtTunai = findViewById(R.id.txtTunai);
        txtKembalian = findViewById(R.id.txtKembalian);
        rvItems = findViewById(R.id.rvItems);
        btnShare = findViewById(R.id.btnShare);
        btnDownload = findViewById(R.id.btnDownload);
        strukContainer = findViewById(R.id.strukContainer);

        rvItems.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadOrderData(String orderId) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("orders").child(orderId);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Set data teks dasar
                    String status = snapshot.child("status").getValue(String.class);
                    long totalBayar = snapshot.child("total_bayar").getValue(Long.class);
                    long timestamp = snapshot.child("timestamp").getValue(Long.class);

                    txtStatus.setText(status);
                    txtKasir.setText("Order ID: " + orderId);
                    txtSubtotal.setText(formatRupiah(totalBayar));

                    // Format tanggal pesanan
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", new Locale("id", "ID"));
                    txtTanggalWaktu.setText(sdf.format(new Date(timestamp)));

                    // Ambil list item pesanan
                    itemList = new ArrayList<>();
                    for (DataSnapshot itemSnap : snapshot.child("items").getChildren()) {
                        CartItem item = itemSnap.getValue(CartItem.class);
                        itemList.add(item);
                    }

                    // Tampilkan ke RecyclerView
                    txtTotalItem.setText(String.valueOf(itemList.size()));
                    adapter = new CartAdapter(BuktiPesanan.this, itemList, null);
                    rvItems.setAdapter(adapter);

                    // Simulasi kembalian (karena ini pesanan online, tunai bisa dianggap pas)
                    txtTunai.setText(formatRupiah(totalBayar));
                    txtKembalian.setText(formatRupiah(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BuktiPesanan.this, "Gagal memuat data pesanan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatRupiah(long amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(amount);
    }

    private void shareStruk() {
        Bitmap bitmap = getBitmapFromView(strukContainer);
        Uri uri = saveBitmap(bitmap, "struk_temp.png");

        if (uri != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share Struk Melalui"));
        }
    }

    private void downloadStruk() {
        Bitmap bitmap = getBitmapFromView(strukContainer);
        String fileName = "Struk_Kotego_" + System.currentTimeMillis() + ".png";
        Uri uri = saveBitmap(bitmap, fileName);

        if (uri != null) {
            Toast.makeText(this, "Struk berhasil disimpan di galeri!", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private Uri saveBitmap(Bitmap bitmap, String fileName) {
        try {
            File cachePath = new File(getCacheDir(), "images");
            cachePath.mkdirs();
            File file = new File(cachePath, fileName);
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            return FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
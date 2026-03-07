package com.example.kotegoid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BuktiPesanan extends AppCompatActivity {

    ImageView btnClose;
    TextView txtTanggalWaktu, txtStatus, txtKasir;
    TextView txtTotalItem, txtSubtotal, txtTunai, txtKembalian;
    RecyclerView rvItems;
    LinearLayout btnShare, btnDownload, strukContainer;

    List<StrukItemModel> itemList;
    StrukAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bukti_pesanan);

        // Initialize views
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

        btnClose.setOnClickListener(v -> finish());

        // Setup RecyclerView
        itemList = new ArrayList<>();
        // Dummy data (ganti dengan data dari Firebase/Intent)
        itemList.add(new StrukItemModel("Ayam Chili padi", 1, 25000));
        itemList.add(new StrukItemModel("blue Lagoon", 1, 15000));
        itemList.add(new StrukItemModel("Air mineral", 1, 5000));

        adapter = new StrukAdapter(itemList);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(adapter);

        // Set data
        txtTanggalWaktu.setText("14 juni 2025, 14.00 WITA");
        txtStatus.setText("Selesai");
        txtKasir.setText("Ani");
        txtTotalItem.setText(String.valueOf(itemList.size()));

        int total = calculateTotal();
        txtSubtotal.setText(formatRupiah(total));
        txtTunai.setText(formatRupiah(50000));
        txtKembalian.setText(formatRupiah(50000 - total));

        // Button listeners
        btnShare.setOnClickListener(v -> shareStruk());
        btnDownload.setOnClickListener(v -> downloadStruk());
    }

    private int calculateTotal() {
        int total = 0;
        for (StrukItemModel item : itemList) {
            total += item.harga * item.qty;
        }
        return total;
    }

    private String formatRupiah(int amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
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
            startActivity(Intent.createChooser(shareIntent, "Share Struk"));
        } else {
            Toast.makeText(this, "Gagal share struk", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadStruk() {
        Bitmap bitmap = getBitmapFromView(strukContainer);
        String fileName = "Struk_" + System.currentTimeMillis() + ".png";
        Uri uri = saveBitmap(bitmap, fileName);

        if (uri != null) {
            Toast.makeText(this, "Struk berhasil disimpan!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Gagal menyimpan struk", Toast.LENGTH_SHORT).show();
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

            return FileProvider.getUriForFile(this,
                    getPackageName() + ".provider", file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
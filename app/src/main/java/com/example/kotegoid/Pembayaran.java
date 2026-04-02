package com.example.kotegoid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Pembayaran extends AppCompatActivity {

    private ImageView btnBack;
    private Button btnUnggahBukti, btnKirim;

    private DatabaseReference db;
    private StorageReference storageRef;
    private FirebaseAuth auth;

    private Uri imageUri;
    private String orderId;
    private int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        // 1. Initialize Firebase
        db = FirebaseDatabase.getInstance().getReference("payments");
        storageRef = FirebaseStorage.getInstance().getReference().child("payment_proofs");
        auth = FirebaseAuth.getInstance();

        // 2. Initialize views
        btnBack = findViewById(R.id.btnBack);
        btnUnggahBukti = findViewById(R.id.btnUnggahBukti);
        btnKirim = findViewById(R.id.btnKirim);

        // 3. Ambil data dari intent (Dikirim dari Keranjang/Checkout)
        orderId = getIntent().getStringExtra("order_id");
        totalPrice = getIntent().getIntExtra("total_price", 0);

        // 4. Button listeners
        btnBack.setOnClickListener(v -> finish());

        btnUnggahBukti.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImage.launch(intent);
        });

        btnKirim.setOnClickListener(v -> uploadBuktiPembayaran());
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    btnUnggahBukti.setText("✅ Gambar Terpilih");
                    Toast.makeText(this, "Gambar berhasil dipilih!", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void uploadBuktiPembayaran() {
        if (imageUri == null) {
            Toast.makeText(this, "Pilih gambar bukti pembayaran terlebih dahulu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lock button agar tidak diklik berkali-kali
        btnKirim.setEnabled(false);
        btnKirim.setText("Sedang Mengirim...");

        String userId = auth.getUid();
        // Nama file unik: pay_ORDERID_WAKTU.jpg
        String fileName = "pay_" + orderId + "_" + System.currentTimeMillis() + ".jpg";
        StorageReference fileRef = storageRef.child(fileName);

        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {

                String paymentId = db.push().getKey();

                // Gunakan model PaymentModel (Sesuaikan urutan parameter)
                PaymentModel payment = new PaymentModel(
                        paymentId,
                        orderId,
                        userId,
                        totalPrice,
                        uri.toString(), // URL gambar dari Storage
                        "Menunggu Verifikasi",
                        System.currentTimeMillis()
                );

                if (paymentId != null) {
                    db.child(paymentId).setValue(payment).addOnSuccessListener(unused -> {

                        // Update status di folder 'orders' agar Admin tahu ada pembayaran masuk
                        FirebaseDatabase.getInstance().getReference("orders")
                                .child(orderId).child("status").setValue("Menunggu Verifikasi");

                        Toast.makeText(this, "Bukti pembayaran berhasil dikirim!", Toast.LENGTH_SHORT).show();

                        // Pindah ke halaman Struk
                        Intent intent = new Intent(Pembayaran.this, BuktiPesanan.class);
                        intent.putExtra("ORDER_ID", orderId);
                        startActivity(intent);
                        finish();

                    }).addOnFailureListener(e -> {
                        btnKirim.setEnabled(true);
                        btnKirim.setText("Kirim");
                        Toast.makeText(this, "Gagal simpan database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }).addOnFailureListener(e -> {
            btnKirim.setEnabled(true);
            btnKirim.setText("Kirim");
            Toast.makeText(this, "Upload gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
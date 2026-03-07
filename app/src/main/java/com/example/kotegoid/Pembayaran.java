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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Pembayaran extends AppCompatActivity {

    ImageView btnBack;
    Button btnUnggahBukti, btnKirim;

    DatabaseReference db;
    StorageReference storageRef;
    FirebaseAuth auth;

    Uri imageUri;
    String orderId;
    int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        // Initialize Firebase
        db = FirebaseDatabase.getInstance().getReference("payments");
        storageRef = FirebaseStorage.getInstance().getReference().child("payment_proofs");
        auth = FirebaseAuth.getInstance();

        // Initialize views
        btnBack = findViewById(R.id.btnBack);
        btnUnggahBukti = findViewById(R.id.btnUnggahBukti);
        btnKirim = findViewById(R.id.btnKirim);

        // Ambil data dari intent
        orderId = getIntent().getStringExtra("order_id");
        totalPrice = getIntent().getIntExtra("total_price", 0);

        // Button listeners
        btnBack.setOnClickListener(v -> finish());
        btnUnggahBukti.setOnClickListener(v -> pilihGambar());
        btnKirim.setOnClickListener(v -> uploadBuktiPembayaran());
    }

    // ActivityResultLauncher untuk pilih gambar
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    Toast.makeText(this, "Gambar terpilih!", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void pilihGambar() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImage.launch(intent);
    }

    private void uploadBuktiPembayaran() {
        if (imageUri == null) {
            Toast.makeText(this, "Pilih gambar bukti pembayaran terlebih dahulu!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getUid();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String fileName = "payment_" + userId + "_" + timestamp + ".jpg";

        StorageReference fileRef = storageRef.child(fileName);

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            // Simpan data pembayaran ke database
                            String paymentId = db.push().getKey();
                            PaymentModel payment = new PaymentModel(
                                    paymentId,
                                    orderId,
                                    userId,
                                    totalPrice,
                                    uri.toString(),
                                    "Pending",
                                    System.currentTimeMillis()
                            );

                            if (paymentId != null) {
                                db.child(paymentId).setValue(payment)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(this,
                                                    "Bukti pembayaran berhasil dikirim!",
                                                    Toast.LENGTH_SHORT).show();
                                            finish();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(this,
                                                        "Gagal menyimpan data: " + e.getMessage(),
                                                        Toast.LENGTH_SHORT).show());
                            }
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Upload gagal: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }
}
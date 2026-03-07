package com.example.kotegoid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Order extends AppCompatActivity {

    TextView txtPilihMeja; // Ganti Spinner jadi TextView
    EditText edtCatatan;
    TextView txtNama, txtHarga, txtJumlah, txtDesc, txtTotal;
    Button btnBayar;
    ImageView imgMenu;

    DatabaseReference db;
    FirebaseAuth auth;

    private String selectedMeja = ""; // Menyimpan meja yang dipilih

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order);

        db = FirebaseDatabase.getInstance().getReference("orders");
        auth = FirebaseAuth.getInstance();

        txtPilihMeja = findViewById(R.id.txtPilihMeja); // TextView untuk pilih meja
        edtCatatan = findViewById(R.id.edtCatatan);
        txtNama = findViewById(R.id.txtNama);
        txtHarga = findViewById(R.id.txtHarga);
        txtJumlah = findViewById(R.id.txtJumlah);
        txtDesc = findViewById(R.id.txtDesc);
        txtTotal = findViewById(R.id.txtTotal);
        imgMenu = findViewById(R.id.imgMenu);
        btnBayar = findViewById(R.id.btnBayar);

        // Ambil data dari intent
        String namaMenu = getIntent().getStringExtra("nama_menu");
        int hargaMenu = getIntent().getIntExtra("harga_menu", 0);
        int jumlahMenu = getIntent().getIntExtra("jumlah_menu", 1);
        String deskripsi = getIntent().getStringExtra("deskripsi");

        // Tampilkan ke UI
        txtNama.setText(namaMenu);
        txtHarga.setText(String.valueOf(hargaMenu));
        txtJumlah.setText(String.valueOf(jumlahMenu));
        txtDesc.setText(deskripsi);
        txtTotal.setText(String.valueOf(hargaMenu * jumlahMenu));

        // Klik untuk buka bottom sheet pilih meja
        txtPilihMeja.setOnClickListener(v -> showBottomSheetPilihMeja());

        btnBayar.setOnClickListener(v -> simpanPesanan());
    }

    private void showBottomSheetPilihMeja() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_pilih_meja, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Set click listener untuk setiap nomor meja
        setupMejaClickListener(bottomSheetView, R.id.btnMeja1, "1", bottomSheetDialog);
        setupMejaClickListener(bottomSheetView, R.id.btnMeja2, "2", bottomSheetDialog);
        setupMejaClickListener(bottomSheetView, R.id.btnMeja3, "3", bottomSheetDialog);
        setupMejaClickListener(bottomSheetView, R.id.btnMeja4, "4", bottomSheetDialog);
        setupMejaClickListener(bottomSheetView, R.id.btnMeja5, "5", bottomSheetDialog);
        setupMejaClickListener(bottomSheetView, R.id.btnMeja6, "6", bottomSheetDialog);

        bottomSheetDialog.show();
    }

    private void setupMejaClickListener(View view, int buttonId, String nomorMeja, BottomSheetDialog dialog) {
        TextView btnMeja = view.findViewById(buttonId);
        btnMeja.setOnClickListener(v -> {
            selectedMeja = nomorMeja;
            txtPilihMeja.setText("Meja " + nomorMeja);
            dialog.dismiss();
            Toast.makeText(this, "Meja " + nomorMeja + " dipilih", Toast.LENGTH_SHORT).show();
        });
    }

    private void simpanPesanan() {

        if (selectedMeja.isEmpty()) {
            Toast.makeText(this, "Silakan pilih meja terlebih dahulu!", Toast.LENGTH_SHORT).show();
            return;
        }

        String catatan = edtCatatan.getText().toString();
        String idUser = auth.getUid();

        // Hitung total harga
        int harga = Integer.parseInt(txtHarga.getText().toString());
        int jumlah = Integer.parseInt(txtJumlah.getText().toString());
        int totalPrice = harga * jumlah;

        // Format tanggal
        String orderDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                .format(new Date());

        // Buat objek PesananModel dengan constructor yang benar
        PesananModel pesanan = new PesananModel(
                idUser,         // user_id
                orderDate,      // order_date
                "Pending",      // order_status
                totalPrice,     // total_price
                selectedMeja,   // meja
                catatan         // catatan
        );

        db.push().setValue(pesanan)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Pesanan berhasil dikirim!", Toast.LENGTH_SHORT).show();
                    finish(); // Kembali ke activity sebelumnya
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
package com.example.kotegoid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Keranjang extends AppCompatActivity implements CartAdapter.OnCartChangeListener {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartList;
    private CheckBox checkboxAll;
    private TextView txtTotalPrice;
    private Button btnCheckout;
    private ImageView btnBackCart;
    private DatabaseReference dbCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        // 1. Inisialisasi Views
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        checkboxAll = findViewById(R.id.checkboxAll);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBackCart = findViewById(R.id.btnBackCart);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartList, this);
        recyclerViewCart.setAdapter(cartAdapter);

        // 2. Ambil data dari Firebase (UserID "user1")
        dbCart = FirebaseDatabase.getInstance().getReference("cart").child("user1");
        dbCart.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    CartItem item = data.getValue(CartItem.class);
                    cartList.add(item);
                }
                cartAdapter.notifyDataSetChanged();
                updateTotalPrice();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Keranjang.this, "Gagal ambil data", Toast.LENGTH_SHORT).show();
            }
        });

        // 3. Tombol Kembali
        btnBackCart.setOnClickListener(v -> finish());

        // 4. Tombol Pilih Semua
        checkboxAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (CartItem item : cartList) {
                item.setChecked(isChecked);
            }
            cartAdapter.notifyDataSetChanged();
            updateTotalPrice();
        });

        // 5. Logika Tombol Checkout
        btnCheckout.setOnClickListener(v -> prosesCheckout());
    }

    private void prosesCheckout() {
        List<CartItem> listCheckout = new ArrayList<>();
        long totalHargaAkhir = 0;

        // Ambil item yang dicentang saja
        for (CartItem item : cartList) {
            if (item.isChecked()) {
                listCheckout.add(item);
                totalHargaAkhir += item.getTotal_price();
            }
        }

        if (listCheckout.isEmpty()) {
            Toast.makeText(this, "Pilih menu yang ingin dipesan!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Pindahkan data ke folder "orders" di Firebase
        DatabaseReference dbOrder = FirebaseDatabase.getInstance().getReference("orders");
        String orderId = dbOrder.push().getKey(); // ID Pesanan unik

        HashMap<String, Object> orderData = new HashMap<>();
        orderData.put("order_id", orderId);
        orderData.put("user_id", "user1");
        orderData.put("total_bayar", totalHargaAkhir);
        orderData.put("status", "Pending"); // Status awal pesanan
        orderData.put("items", listCheckout);
        orderData.put("timestamp", System.currentTimeMillis());

        if (orderId != null) {
            dbOrder.child(orderId).setValue(orderData).addOnSuccessListener(unused -> {
                // Hapus item yang sudah dipesan dari keranjang Firebase
                for (CartItem checkedItem : listCheckout) {
                    dbCart.child(checkedItem.getMenu_id()).removeValue();
                }

                Toast.makeText(this, "Pesanan Berhasil Dikirim!", Toast.LENGTH_LONG).show();

                // PINDAH KE HALAMAN BUKTI PESANAN
                Intent intent = new Intent(Keranjang.this, BuktiPesanan.class);
                intent.putExtra("ORDER_ID", orderId); // Mengirim ID Pesanan agar BuktiPesanan bisa ambil data yang benar
                startActivity(intent);
                finish(); // Tutup halaman keranjang
            });
        }
    }

    @Override
    public void onCartChanged() {
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        long total = 0;
        int checkedCount = 0;

        for (CartItem item : cartList) {
            if (item.isChecked()) {
                total += item.getTotal_price();
                checkedCount++;
            }
        }

        checkboxAll.setChecked(checkedCount == cartList.size() && cartList.size() > 0);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        txtTotalPrice.setText("Total: " + formatRupiah.format(total));
    }
}
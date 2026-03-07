package com.example.kotegoid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        // Inisialisasi views
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        checkboxAll = findViewById(R.id.checkboxAll);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBackCart = findViewById(R.id.btnBackCart);

        // Setup RecyclerView
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        // Data dummy keranjang (nanti bisa diambil dari database atau SharedPreferences)
        cartList = new ArrayList<>();
        cartList.add(new CartItem("Ayam Chili Padi", R.drawable.miepedas, 25000));
        cartList.add(new CartItem("Ayam Chili Padi", R.drawable.miepedas, 25000));
        cartList.add(new CartItem("Ayam Chili Padi", R.drawable.miepedas, 25000));
        cartList.add(new CartItem("Ayam Chili Padi", R.drawable.miepedas, 25000));

        // Set adapter
        cartAdapter = new CartAdapter(cartList, this);
        recyclerViewCart.setAdapter(cartAdapter);

        // Tombol Back
        btnBackCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Checkbox Semua
        checkboxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Pilih/hapus semua item
                for (CartItem item : cartList) {
                    item.setChecked(isChecked);
                }
                cartAdapter.notifyDataSetChanged();
                updateTotalPrice();
            }
        });

        // Tombol Checkout
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalChecked = 0;
                for (CartItem item : cartList) {
                    if (item.isChecked()) {
                        totalChecked++;
                    }
                }

                if (totalChecked > 0) {
                    Toast.makeText(Keranjang.this, "Checkout " + totalChecked + " item", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Keranjang.this, "Pilih item terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Update total harga awal
        updateTotalPrice();
    }

    @Override
    public void onCartChanged() {
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        int total = 0;
        int checkedCount = 0;

        for (CartItem item : cartList) {
            if (item.isChecked()) {
                total += item.getPrice();
                checkedCount++;
            }
        }

        // Update checkbox "Semua"
        checkboxAll.setChecked(checkedCount == cartList.size() && cartList.size() > 0);

        // Format total harga
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formattedTotal = rupiahFormat.format(total);
        txtTotalPrice.setText("Total: " + formattedTotal);
    }
}
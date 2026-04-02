package com.example.kotegoid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Import Glide
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class MenuAdapterAdmin extends RecyclerView.Adapter<MenuAdapterAdmin.MenuViewHolder> {

    private Context context;
    private List<MenuAdmin> listMenu;

    public MenuAdapterAdmin(Context context, List<MenuAdmin> listMenu) {
        this.context = context;
        this.listMenu = listMenu;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu_admin, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuAdmin menu = listMenu.get(position);

        // 1. Set Teks (Nama, Harga, Kategori)
        holder.tvNama.setText(": " + menu.getMenu_name());
        holder.tvHarga.setText(": Rp " + String.valueOf(menu.getPrice()));
        holder.tvKategori.setText(": " + menu.getCategory());
        holder.tvStatus.setText(": Tersedia");

        // 2. Tampilkan Gambar menggunakan Glide
        if (menu.getImage_url() != null && !menu.getImage_url().isEmpty()) {
            Glide.with(context)
                    .load(menu.getImage_url())
                    .placeholder(R.drawable.miepedas) // Gambar saat proses loading
                    .error(R.drawable.miepedas)       // Gambar jika link rusak
                    .into(holder.imgMenu);
        } else {
            // Jika tidak ada URL gambar, pakai gambar placeholder
            holder.imgMenu.setImageResource(R.drawable.miepedas);
        }

        // 3. Tombol Edit
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, TambahMenu.class);
            intent.putExtra("menu_id", menu.getMenu_id());
            intent.putExtra("menu_name", menu.getMenu_name());
            intent.putExtra("price", menu.getPrice());
            intent.putExtra("category", menu.getCategory());
            intent.putExtra("discription", menu.getDiscription());
            // Kirim link gambar juga agar di halaman edit gambarnya muncul
            intent.putExtra("image_url", menu.getImage_url());
            context.startActivity(intent);
        });

        // 4. Tombol Hapus
        holder.btnHapus.setOnClickListener(v -> {
            showDeleteDialog(menu.getMenu_id(), menu.getMenu_name());
        });
    }

    private void showDeleteDialog(String menuId, String namaMenu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Hapus Menu");
        builder.setMessage("Hapus Menu " + namaMenu + " ini secara permanen?");

        builder.setPositiveButton("Ya", (dialog, which) -> {
            FirebaseDatabase.getInstance().getReference("menu").child(menuId)
                    .removeValue().addOnSuccessListener(unused -> {
                        Toast.makeText(context, namaMenu + " Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                    });
        });

        builder.setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvHarga, tvKategori, tvStatus;
        ImageView imgMenu;
        Button btnEdit, btnHapus;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama_admin);
            tvHarga = itemView.findViewById(R.id.tv_harga_admin);
            tvKategori = itemView.findViewById(R.id.tv_kategori_admin);
            tvStatus = itemView.findViewById(R.id.tv_status_admin);
            imgMenu = itemView.findViewById(R.id.img_menu_admin);
            btnEdit = itemView.findViewById(R.id.btn_edit_admin);
            btnHapus = itemView.findViewById(R.id.btn_hapus_admin);
        }
    }
}
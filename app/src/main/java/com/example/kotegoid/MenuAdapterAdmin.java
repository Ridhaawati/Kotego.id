package com.example.kotegoid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent; // Tambahkan import ini
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

        holder.tvNama.setText(": " + menu.getNama());
        holder.tvHarga.setText(": " + menu.getHarga());
        holder.tvKategori.setText(": " + menu.getKategori());
        holder.tvStatus.setText(": " + menu.getStatus());

        // Update Tombol Edit untuk Pindah ke Halaman TambahMenu (Mode Edit)
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, TambahMenu.class);
            // Mengirim data menu agar muncul di form edit
            intent.putExtra("nama", menu.getNama());
            intent.putExtra("harga", menu.getHarga());
            intent.putExtra("kategori", menu.getKategori());
            intent.putExtra("status", menu.getStatus());
            intent.putExtra("deskripsi", menu.getDeskripsi()); // Jika ada deskripsi
            context.startActivity(intent);
        });

        holder.btnHapus.setOnClickListener(v -> {
            showDeleteDialog(position, menu.getNama());
        });
    }

    private void showDeleteDialog(int position, String namaMenu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Hapus Menu");
        builder.setMessage("Hapus Menu " + namaMenu + " ini ?");

        builder.setPositiveButton("Ya", (dialog, which) -> {
            listMenu.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, listMenu.size());
            Toast.makeText(context, namaMenu + " Berhasil Dihapus", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Tidak", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
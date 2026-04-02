package com.example.kotegoid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Import Glide untuk gambar online
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    // Gunakan MenuAdmin sebagai model datanya supaya sama dengan database
    private List<MenuAdmin> menuList;
    private Context context;

    public MenuAdapter(Context context, List<MenuAdmin> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuAdmin menu = menuList.get(position);

        // 1. Set Nama dan Harga (Penting untuk pembeli!)
        holder.menuName.setText(menu.getMenu_name());
        holder.menuPrice.setText("Rp " + String.valueOf(menu.getPrice()));

        // 2. Load Gambar dari Firebase pakai Glide
        if (menu.getImage_url() != null && !menu.getImage_url().isEmpty()) {
            Glide.with(context)
                    .load(menu.getImage_url())
                    .placeholder(R.drawable.miepedas)
                    .into(holder.menuImage);
        } else {
            holder.menuImage.setImageResource(R.drawable.miepedas);
        }

        // 3. Klik item untuk pindah ke ProductDescription (Detail Produk)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDescription.class);
            // Kirim semua data menu ke halaman detail
            intent.putExtra("menu_id", menu.getMenu_id());
            intent.putExtra("menu_name", menu.getMenu_name());
            intent.putExtra("price", menu.getPrice());
            intent.putExtra("image_url", menu.getImage_url());
            intent.putExtra("description", menu.getDiscription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView menuImage;
        TextView menuName, menuPrice; // Pastikan menuPrice ada di sini

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuImage = itemView.findViewById(R.id.menuImage);
            menuName = itemView.findViewById(R.id.menuName);
            menuPrice = itemView.findViewById(R.id.menuPrice); // Pastikan ID ini ada di item_menu.xml
        }
    }
}
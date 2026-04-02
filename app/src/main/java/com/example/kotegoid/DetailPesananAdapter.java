package com.example.kotegoid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DetailPesananAdapter extends RecyclerView.Adapter<DetailPesananAdapter.ViewHolder> {

    // Ganti OrderItemModel menjadi CartItem
    private List<CartItem> itemList;

    public DetailPesananAdapter(List<CartItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detail_pesanan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = itemList.get(position);

        // Gunakan getter dari CartItem
        holder.txtNamaMenu.setText(item.getMenu_name());
        holder.txtJumlahHarga.setText(item.getQuantity() + " x @ Rp " + String.format("%,d", item.getPrice()));
        holder.txtSubtotal.setText("Subtotal : Rp " + String.format("%,d", item.getTotal_price()));

        // Load image asli dari Firebase URL menggunakan Glide
        Glide.with(holder.itemView.getContext())
                .load(item.getImage_url())
                .placeholder(R.drawable.miepedas)
                .into(holder.imgMenuItem);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMenuItem;
        TextView txtNamaMenu, txtJumlahHarga, txtSubtotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMenuItem = itemView.findViewById(R.id.imgMenuItem);
            txtNamaMenu = itemView.findViewById(R.id.txtNamaMenu);
            txtJumlahHarga = itemView.findViewById(R.id.txtJumlahHarga);
            txtSubtotal = itemView.findViewById(R.id.txtSubtotal);
        }
    }
}
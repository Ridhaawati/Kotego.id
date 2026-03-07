package com.example.kotegoid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DetailPesananAdapter extends RecyclerView.Adapter<DetailPesananAdapter.ViewHolder> {

    private List<OrderItemModel> itemList;

    public DetailPesananAdapter(List<OrderItemModel> itemList) {
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
        OrderItemModel item = itemList.get(position);

        holder.txtNamaMenu.setText(item.namaMenu);
        holder.txtJumlahHarga.setText(item.jumlah + " x @ Rp " +
                String.format("%,d", item.harga));
        holder.txtSubtotal.setText("Subtotal : " +
                String.format("%,d", item.jumlah * item.harga));

        // Set image jika ada (gunakan Glide atau Picasso)
        // Glide.with(holder.itemView.getContext())
        //     .load(item.imageUrl)
        //     .into(holder.imgMenuItem);
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
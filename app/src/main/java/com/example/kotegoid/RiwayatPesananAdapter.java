package com.example.kotegoid;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RiwayatPesananAdapter extends RecyclerView.Adapter<RiwayatPesananAdapter.ViewHolder> {

    private List<RiwayatPesananModel> riwayatList;

    public RiwayatPesananAdapter(List<RiwayatPesananModel> riwayatList) {
        this.riwayatList = riwayatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_riwayat_pesanan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RiwayatPesananModel item = riwayatList.get(position);

        holder.txtTanggalPesanan.setText(item.tanggal);
        holder.txtWaktuPesanan.setText(item.waktu);
        holder.txtStatusPesanan.setText(item.status);

        // Set warna status
        holder.txtStatusPesanan.setTextColor(item.getStatusColor());

        // Set background status
        GradientDrawable bgShape = (GradientDrawable) holder.txtStatusPesanan.getBackground();
        if (bgShape != null) {
            bgShape.setColor(item.getStatusBgColor());
        }

        // Click listener untuk buka detail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailPesanan.class);
            intent.putExtra("order_id", item.orderId);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return riwayatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTanggalPesanan, txtWaktuPesanan, txtStatusPesanan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTanggalPesanan = itemView.findViewById(R.id.txtTanggalPesanan);
            txtWaktuPesanan = itemView.findViewById(R.id.txtWaktuPesanan);
            txtStatusPesanan = itemView.findViewById(R.id.txtStatusPesanan);
        }
    }
}
package com.example.kotegoid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Gunakan library Glide untuk load gambar
import java.util.List;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.NotifViewHolder> {

    private Context context;
    private List<NotifikasiModel> listNotif;

    public NotifikasiAdapter(Context context, List<NotifikasiModel> listNotif) {
        this.context = context;
        this.listNotif = listNotif;
    }

    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Menghubungkan ke layout item_notifikasi.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_notifikasi, parent, false);
        return new NotifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        NotifikasiModel notif = listNotif.get(position);

        // Set data ke TextView
        holder.tvNama.setText(": " + notif.getNamaMenu());
        holder.tvHarga.setText(": " + notif.getHarga());
        holder.tvKategori.setText(": " + notif.getKategori());
        holder.tvStatus.setText(": " + notif.getStatus());

        // Load gambar menggunakan Glide (jika ada URL-nya)
        if (notif.getImageUrl() != null && !notif.getImageUrl().isEmpty()) {
            Glide.with(context).load(notif.getImageUrl()).into(holder.imgNotif);
        } else {
            holder.imgNotif.setImageResource(R.drawable.download); // Gambar default
        }

        // Aksi tombol Lihat Detail
        holder.btnDetail.setOnClickListener(v -> {
            Toast.makeText(context, "Detail Pesanan: " + notif.getNamaMenu(), Toast.LENGTH_SHORT).show();
            // Jika nanti ada Activity Detail, tambahkan Intent di sini
        });
    }

    @Override
    public int getItemCount() {
        return listNotif.size();
    }

    public static class NotifViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvHarga, tvKategori, tvStatus;
        ImageView imgNotif;
        Button btnDetail;

        public NotifViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama_notif);
            tvHarga = itemView.findViewById(R.id.tv_harga_notif);
            tvKategori = itemView.findViewById(R.id.tv_kategori_notif);
            tvStatus = itemView.findViewById(R.id.tv_status_notif);
            imgNotif = itemView.findViewById(R.id.img_notif);
            btnDetail = itemView.findViewById(R.id.btn_lihat_detail);
        }
    }
}
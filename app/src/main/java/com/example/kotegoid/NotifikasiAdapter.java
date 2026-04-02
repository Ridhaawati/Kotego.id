package com.example.kotegoid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.ViewHolder> {

    private Context context;
    private List<NotifikasiModel> list;

    public NotifikasiAdapter(Context context, List<NotifikasiModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Menggunakan layout item_notifikasi sesuai nama file XML kamu
        View view = LayoutInflater.from(context).inflate(R.layout.item_notifikasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotifikasiModel model = list.get(position);

        // Menampilkan status pesanan
        holder.tvStatus.setText(": " + model.getStatus());

        // Mengambil data item pertama dari list pesanan untuk preview
        if (model.getItems() != null && !model.getItems().isEmpty()) {
            CartItem firstItem = model.getItems().get(0);
            holder.tvNama.setText(": " + firstItem.getMenu_name());
            holder.tvHarga.setText(": Rp " + String.valueOf(model.getTotal_bayar()));

            // Load gambar menu menggunakan Glide
            Glide.with(context)
                    .load(firstItem.getImage_url())
                    .placeholder(R.drawable.miepedas)
                    .into(holder.imgNotif);
        }

        // Klik tombol "Lihat Detail Pesanan" untuk ke DetailPesananAdmin
        holder.btnLihatDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailPesananAdmin.class);
            intent.putExtra("idPesanan", model.getOrder_id());
            intent.putExtra("namaPembeli", model.getUser_id());
            intent.putExtra("harga", "Rp " + model.getTotal_bayar());

            if (model.getItems() != null && !model.getItems().isEmpty()) {
                intent.putExtra("namaMenu", model.getItems().get(0).getMenu_name());
                intent.putExtra("image", model.getItems().get(0).getImage_url());
            }

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgNotif;
        TextView tvNama, tvHarga, tvStatus;
        Button btnLihatDetail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inisialisasi ID sesuai dengan yang ada di item_notifikasi.xml
            imgNotif = itemView.findViewById(R.id.img_notif);
            tvNama = itemView.findViewById(R.id.tv_nama_notif);
            tvHarga = itemView.findViewById(R.id.tv_harga_notif);
            tvStatus = itemView.findViewById(R.id.tv_status_notif);
            btnLihatDetail = itemView.findViewById(R.id.btn_lihat_detail);
        }
    }
}
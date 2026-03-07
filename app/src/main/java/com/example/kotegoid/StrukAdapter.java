package com.example.kotegoid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class StrukAdapter extends RecyclerView.Adapter<StrukAdapter.ViewHolder> {

    private List<StrukItemModel> itemList;

    public StrukAdapter(List<StrukItemModel> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_struk, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StrukItemModel item = itemList.get(position);

        holder.txtNamaItem.setText(item.namaItem);
        holder.txtQty.setText(String.valueOf(item.qty));
        holder.txtHarga.setText(formatRupiah(item.harga));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private String formatRupiah(int amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
        return formatter.format(amount);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNamaItem, txtQty, txtHarga;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamaItem = itemView.findViewById(R.id.txtNamaItem);
            txtQty = itemView.findViewById(R.id.txtQty);
            txtHarga = itemView.findViewById(R.id.txtHarga);
        }
    }
}
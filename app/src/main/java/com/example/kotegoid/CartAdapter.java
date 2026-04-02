package com.example.kotegoid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartList;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, List<CartItem> cartList, OnCartChangeListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartList.get(position);

        holder.tvName.setText(item.getMenu_name());
        holder.tvPrice.setText("Rp " + item.getPrice() + " x" + item.getQuantity());
        holder.checkboxItem.setChecked(item.isChecked());

        // Load Gambar dari URL Firebase
        Glide.with(context)
                .load(item.getImage_url())
                .placeholder(R.drawable.miepedas)
                .into(holder.imgItem);

        holder.checkboxItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
            listener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        TextView tvName, tvPrice;
        CheckBox checkboxItem;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            // Sesuaikan dengan ID yang ada di XML item_cart.xml kamu
            imgItem = itemView.findViewById(R.id.cartItemImage);
            tvName = itemView.findViewById(R.id.cartItemName);
            tvPrice = itemView.findViewById(R.id.cartItemPrice);
            checkboxItem = itemView.findViewById(R.id.checkboxItem);
        }
    }
}
package com.example.kotegoid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartList;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(List<CartItem> cartList, OnCartChangeListener listener) {
        this.cartList = cartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartList.get(position);

        holder.cartItemName.setText(cartItem.getName());
        holder.cartItemImage.setImageResource(cartItem.getImageResId());

        // Format harga dengan Rupiah
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formattedPrice = rupiahFormat.format(cartItem.getPrice());
        holder.cartItemPrice.setText(formattedPrice);

        // Set checkbox
        holder.checkboxItem.setChecked(cartItem.isChecked());

        // Listener untuk checkbox
        holder.checkboxItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cartItem.setChecked(isChecked);
                if (listener != null) {
                    listener.onCartChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkboxItem;
        ImageView cartItemImage;
        TextView cartItemName, cartItemPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxItem = itemView.findViewById(R.id.checkboxItem);
            cartItemImage = itemView.findViewById(R.id.cartItemImage);
            cartItemName = itemView.findViewById(R.id.cartItemName);
            cartItemPrice = itemView.findViewById(R.id.cartItemPrice);
        }
    }
}
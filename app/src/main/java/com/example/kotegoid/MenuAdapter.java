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

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItem> menuList;
    private Context context;

    public MenuAdapter(List<MenuItem> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem menuItem = menuList.get(position);
        holder.menuName.setText(menuItem.getName());
        holder.menuImage.setImageResource(menuItem.getImageResId());

        // Klik item untuk pindah ke ProductDescription
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDescription.class);
                intent.putExtra("menuName", menuItem.getName());
                intent.putExtra("menuImage", menuItem.getImageResId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView menuImage;
        TextView menuName;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            menuImage = itemView.findViewById(R.id.menuImage);
            menuName = itemView.findViewById(R.id.menuName);
        }
    }
}
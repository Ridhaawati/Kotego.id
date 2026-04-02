package com.example.kotegoid;

public class CartItem {
    private String menu_id, menu_name, image_url;
    private long price, total_price;
    private int quantity;
    private boolean isChecked = false;

    // Wajib ada constructor kosong untuk Firebase
    public CartItem() {}

    public String getMenu_id() { return menu_id; }
    public String getMenu_name() { return menu_name; }
    public String getImage_url() { return image_url; }
    public long getPrice() { return price; }
    public long getTotal_price() { return total_price; }
    public int getQuantity() { return quantity; }

    public boolean isChecked() { return isChecked; }
    public void setChecked(boolean checked) { isChecked = checked; }
}
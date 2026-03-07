package com.example.kotegoid;

public class CartItem {
    private String name;
    private int imageResId;
    private int price;
    private boolean isChecked;

    public CartItem(String name, int imageResId, int price) {
        this.name = name;
        this.imageResId = imageResId;
        this.price = price;
        this.isChecked = false;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getPrice() {
        return price;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
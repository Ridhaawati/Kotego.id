package com.example.kotegoid;

public class PesananModel {

    public String user_id;
    public String order_date;
    public String order_status;
    public String meja;
    public String catatan;

    public int total_price;

    public PesananModel() {
        // Diperlukan Firebase
    }

    public PesananModel(String user_id, String order_date, String order_status,
                        int total_price, String meja, String catatan) {

        this.user_id = user_id;
        this.order_date = order_date;
        this.order_status = order_status;
        this.total_price = total_price;
        this.meja = meja;
        this.catatan = catatan;
    }
}

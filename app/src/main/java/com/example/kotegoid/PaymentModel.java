package com.example.kotegoid;

public class PaymentModel {
    public String payment_id;
    public String order_id;
    public String user_id;
    public int total_price;
    public String proof_image_url;
    public String status;
    public long timestamp;

    public PaymentModel() {
        // Required for Firebase
    }

    public PaymentModel(String payment_id, String order_id, String user_id,
                        int total_price, String proof_image_url, String status,
                        long timestamp) {
        this.payment_id = payment_id;
        this.order_id = order_id;
        this.user_id = user_id;
        this.total_price = total_price;
        this.proof_image_url = proof_image_url;
        this.status = status;
        this.timestamp = timestamp;
    }
}
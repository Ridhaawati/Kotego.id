package com.example.kotegoid;

public class UserData {
    private String name;
    private String email;
    private String role;
    private String photoUrl; // Tambahkan ini supaya sama dengan database kamu

    public UserData() {}

    public UserData(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.photoUrl = ""; // Kasih string kosong sebagai default
    }

    // Getter dan Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
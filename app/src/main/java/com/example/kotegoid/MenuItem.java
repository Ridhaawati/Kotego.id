package com.example.kotegoid;

public class MenuItem {
        private String name;
        private int imageResId;

        public MenuItem(String name, int imageResId) {
            this.name = name;
            this.imageResId = imageResId;
        }

        public String getName() {
            return name;
        }

        public int getImageResId() {
            return imageResId;
        }
}

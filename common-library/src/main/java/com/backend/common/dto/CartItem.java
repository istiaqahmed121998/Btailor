package com.backend.common.dto;

public class CartItem {
    private String variantSku;
    private int quantity;
    private double price;
    private String productName;
    private String color;
    private String size;
    private String thumbnail;

    public CartItem() {
    }

    public CartItem(String variantSku, int quantity, double price, String productName, String color, String size, String thumbnail) {
        this.variantSku = variantSku;
        this.quantity = quantity;
        this.price = price;
        this.productName = productName;
        this.color = color;
        this.size = size;
        this.thumbnail = thumbnail;
    }

    public String getVariantSku() {
        return variantSku;
    }

    public void setVariantSku(String variantSku) {
        this.variantSku = variantSku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
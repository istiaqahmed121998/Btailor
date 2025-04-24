package com.backend.common.dto;

public  class OrderItem {
    private String productName;
    private String variantSku;
    private int quantity;
    private Double price;
    private String color;
    private String size;
    private String thumbnail;

    public OrderItem() {}

    public OrderItem(String productName, String variantSku, int quantity, Double price, String color, String size, String thumbnail) {
        this.productName = productName;
        this.variantSku = variantSku;
        this.quantity = quantity;
        this.price = price;
        this.color = color;
        this.size = size;
        this.thumbnail = thumbnail;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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


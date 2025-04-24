package com.backend.cartservice.application.dto;

public class ProductSnapshotDto {
    private String productName;
    private String color;
    private String size;
    private String thumbnail;
    private double price;

    public ProductSnapshotDto(String productName, String color, String size, String thumbnail, double price) {
        this.productName = productName;
        this.color = color;
        this.size = size;
        this.thumbnail = thumbnail;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
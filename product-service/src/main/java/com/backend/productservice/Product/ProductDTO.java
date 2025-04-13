package com.backend.productservice.Product;

public class ProductDTO {
    private String name;
    private double price;
    private String description;
    private Long categoryId;
    private String tag;

    public ProductDTO() {}

    public ProductDTO(String name, double price, String description, Long categoryId, String tag) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.categoryId = categoryId;
        this.tag = tag;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public Long getCategoryId() { return categoryId; }
    public String getTag() { return tag; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void setTag(String tag) { this.tag = tag; }
}

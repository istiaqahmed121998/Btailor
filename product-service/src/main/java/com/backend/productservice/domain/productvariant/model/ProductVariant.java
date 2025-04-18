package com.backend.productservice.domain.productvariant.model;

import com.backend.productservice.domain.product.model.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
@Entity
@Table(name = "product_variants")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String color;
    private String size;
    private String material;
    private Double price;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductVariant() {}

    public ProductVariant(Long id, String sku, String color, String size, String material, Double price, Product product) {
        this.id = id;
        this.sku = sku;
        this.color = color;
        this.size = size;
        this.material = material;
        this.price = price;
        this.product = product;
    }


    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

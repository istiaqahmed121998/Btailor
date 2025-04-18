package com.backend.productservice.domain.productimage.model;

import com.backend.productservice.domain.product.model.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
@Entity
@Table(name = "product_images")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private String altText;

    private Boolean isPrimary;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage(Long id, String imageUrl, String altText, Boolean isPrimary, Product product) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.isPrimary = isPrimary;
        this.product = product;
    }

    public ProductImage() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

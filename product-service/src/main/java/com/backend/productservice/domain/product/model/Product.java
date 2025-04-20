package com.backend.productservice.domain.product.model;


import com.backend.productservice.domain.category.model.Category;
import com.backend.productservice.domain.Tag.model.Tag;
import com.backend.productservice.domain.productimage.model.ProductImage;
import com.backend.productservice.domain.productvariant.model.ProductVariant;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductVariant> variants = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();
    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "product_tags",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();


    public Product() {
    }
    private Long sellerId;
    public Product(Long id, String name, String description,Category category, Set<ProductVariant> variants, Set<ProductImage> images, Set<Tag> tags, Long sellerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.variants = variants;
        this.images = images;
        this.tags = tags;
        this.sellerId = sellerId;
    }

    public void addImage(ProductImage image) {
        image.setProduct(this);
        this.images.add(image);
    }
    public void addVariant(ProductVariant productVariant) {
        productVariant.setProduct(this);
        this.variants.add(productVariant);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<ProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(Set<ProductVariant> variants) {
        this.variants = variants;
    }

    public Set<ProductImage> getImages() {
        return images;
    }

    public void setImages(Set<ProductImage> images) {
        this.images = images;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long userId) {
        this.sellerId = userId;
    }



}

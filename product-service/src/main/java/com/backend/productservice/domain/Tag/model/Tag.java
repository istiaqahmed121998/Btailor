package com.backend.productservice.domain.Tag.model;

import com.backend.productservice.domain.product.model.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    @JsonBackReference
    @ManyToMany(mappedBy = "tags")
    private Set<Product> products = new HashSet<>();

    public Tag() {}

    public Tag(Long id, String name, Set<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}

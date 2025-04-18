package com.backend.productservice.domain.category.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;


import java.util.HashSet;
import java.util.Set;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference  // Prevents infinite recursion
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference  // Handles the child category list properly
    private Set<Category> subCategories = new HashSet<>();

    // Constructors
    public Category() {}

    public Category(String name, Category parentCategory) {
        this.name = name;
        this.parentCategory = parentCategory;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Category getParentCategory() { return parentCategory; }

    public void setParentCategory(Category parentCategory) { this.parentCategory = parentCategory; }

    public Set<Category> getSubCategories() { return subCategories; }

    public void setSubCategories(Set<Category> subCategories) { this.subCategories = subCategories; }

    public void addSubCategory(Category subCategory) {
        subCategories.add(subCategory);
        subCategory.setParentCategory(this);
    }

    public void removeSubCategory(Category subCategory) {
        subCategories.remove(subCategory);
        subCategory.setParentCategory(null);
    }
}

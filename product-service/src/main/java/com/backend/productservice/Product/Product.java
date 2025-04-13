package com.backend.productservice.Product;


import com.backend.productservice.Category.Category;
import jakarta.persistence.*;


@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column
    private String description;

    @Column
    private String tag;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {}

    public Product(String name, double price, String description, String tag, Category category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.tag = tag;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
}

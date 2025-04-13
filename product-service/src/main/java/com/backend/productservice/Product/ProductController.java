package com.backend.productservice.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/tag/{tag}")
    public List<Product> getProductsByTag(@PathVariable String tag) {
        return productService.getProductsByTag(tag);
    }

    @PostMapping
    public Product createProduct(@RequestBody ProductDTO dto) {
        return productService.createProduct(dto);
    }
}

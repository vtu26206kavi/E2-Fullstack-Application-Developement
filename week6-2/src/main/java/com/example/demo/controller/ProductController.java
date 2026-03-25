package com.example.demo.controller;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService ProductService;
    // POST /api/products
    // Add a new product (Task 6.1)
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product savedProduct = ProductService.addProduct(product);
        return new ResponseEntity<>(savedProduct,HttpStatus.CREATED); 
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = ProductService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK); 
    }

    // ---------------------------------------------------------------
    // GET /api/products/{id}
    // Retrieve a specific product by Product ID (Task 6.2)
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {

        Optional<Product> product = ProductService.getProductById(id);

        if (product.isPresent()) {
            // Product found  → return product JSON with 200 OK
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        } else {
            // Product not found → return 404 Not Found with empty body
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
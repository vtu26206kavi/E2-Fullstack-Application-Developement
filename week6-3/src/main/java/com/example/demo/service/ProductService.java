package com.example.demo.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.product;

@Service
public class ProductService {

	    // In-memory product list (acts as a mock database)
	    private List<product> productList = new ArrayList<>();

	    public ProductService() {
	        // Pre-loaded sample data
	        productList.add(new product(1, "Laptop",     "Electronics", 75000.00, 10));
	        productList.add(new product(2, "Smartphone", "Electronics", 40000.00, 25));
	        productList.add(new product(3, "Headphones", "Accessories", 3500.00,  50));
	        productList.add(new product(4, "Keyboard",   "Accessories", 1800.00,  30));
	    }

	    // Get all products
	    public List<product> getAllProducts() {
	        return productList;
	    }

	    // Get product by ID
	    public Optional<product> getProductById(int id) {
	        return productList.stream()
	                          .filter(p -> p.getId() == id)
	                          .findFirst();
	    }

	    // PUT - Update existing product
	    public product updateProduct(int id, product updatedProduct) {
	        for (int i = 0; i < productList.size(); i++) {
	            if (productList.get(i).getId() == id) {
	                updatedProduct.setId(id); // Keep original ID
	                productList.set(i, updatedProduct);
	                return updatedProduct;
	            }
	        }
	        return null; // Not found
	    }

	    // DELETE - Delete product by ID
	    public boolean deleteProduct(int id) {
	        return productList.removeIf(p -> p.getId() == id);
	    }
	}

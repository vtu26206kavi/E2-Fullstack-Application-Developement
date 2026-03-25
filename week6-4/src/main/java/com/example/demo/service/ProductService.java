package com.example.demo.service;


import com.example.demo.model.Product;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
	
	public class ProductService {

	    private List<Product> productList = new ArrayList<>();

	    public ProductService() {
	        productList.add(new Product(1, "Laptop",     "Electronics", 75000.00, 10));
	        productList.add(new Product(2, "Smartphone", "Electronics", 40000.00, 25));
	        productList.add(new Product(3, "Headphones", "Accessories", 3500.00,  50));
	        productList.add(new Product(4, "Keyboard",   "Accessories", 1800.00,  30));
	        productList.add(new Product(5, "Monitor",    "Electronics", 18000.00, 15));
	    }

	    public List<Product> getAllProducts() {
	        return productList;
	    }

	    // Filter by category using @RequestParam
	    public List<Product> getByCategory(String category) {
	        return productList.stream()
	                .filter(p -> p.getCategory().equalsIgnoreCase(category))
	                .collect(Collectors.toList());
	    }

	    // Search by name keyword using @RequestParam
	    public List<Product> searchByName(String keyword) {
	        return productList.stream()
	                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
	                .collect(Collectors.toList());
	    }

	    public Optional<Product> getProductById(int id) {
	        return productList.stream().filter(p -> p.getId() == id).findFirst();
	    }

	    public Product addProduct(Product product) {
	        int newId = productList.stream().mapToInt(Product::getId).max().orElse(0) + 1;
	        product.setId(newId);
	        productList.add(product);
	        return product;
	    }

	    public Product updateProduct(int id, Product updated) {
	        for (int i = 0; i < productList.size(); i++) {
	            if (productList.get(i).getId() == id) {
	                updated.setId(id);
	                productList.set(i, updated);
	                return updated;
	            }
	        }
	        return null;
	    }

	    public boolean deleteProduct(int id) {
	        return productList.removeIf(p -> p.getId() == id);
	    }
	}
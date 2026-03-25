package com.example.demo.controller;


	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.product;
import com.example.demo.service.ProductService;

	@RestController
	@RequestMapping("/api/products")
	public class ProductController {

	    @Autowired
	    private ProductService productService;

	    // GET all products
	    @GetMapping
	    public ResponseEntity<List<product>> getAllProducts() {
	        return ResponseEntity.ok(productService.getAllProducts());
	    }

	    // GET product by ID
	    @GetMapping("/{id}")
	    public ResponseEntity<?> getProductById(@PathVariable int id) {
	        Optional<product> product = productService.getProductById(id);
	        if (product.isPresent()) {
	            return ResponseEntity.ok(product.get());
	        }
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body(errorResponse("Product with ID " + id + " not found."));
	    }

	    // PUT - Update product by ID
	    @PutMapping("/{id}")
	    public ResponseEntity<?> updateProduct(@PathVariable int id,
	                                           @RequestBody product updatedProduct) {
	        product product = productService.updateProduct(id, updatedProduct);
	        if (product != null) {
	            return ResponseEntity.ok(product);
	        }
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body(errorResponse("Product with ID " + id + " not found. Cannot update."));
	    }

	    // DELETE - Delete product by ID
	    @DeleteMapping("/{id}")
	    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
	        boolean deleted = productService.deleteProduct(id);
	        if (deleted) {
	            Map<String, String> response = new HashMap<>();
	            response.put("message", "Product with ID " + id + " deleted successfully.");
	            return ResponseEntity.ok(response);
	        }
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body(errorResponse("Product with ID " + id + " not found. Cannot delete."));
	    }

	    // Helper: error response body
	    private Map<String, String> errorResponse(String message) {
	        Map<String, String> error = new HashMap<>();
	        error.put("error", message);
	        return error;
	    }
	}
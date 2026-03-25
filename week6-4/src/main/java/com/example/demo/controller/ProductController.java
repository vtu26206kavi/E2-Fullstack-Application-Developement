package com.example.demo.controller;


	import com.example.demo.model.Product;
	import com.example.demo.service.ProductService;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.web.bind.annotation.*;

	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	import java.util.Optional;

	@RestController
	@RequestMapping("/api/products")
	public class ProductController {

	    @Autowired
	    private ProductService productService;

	    @GetMapping
	    public ResponseEntity<List<Product>> getAllProducts() {
	        List<Product> products = productService.getAllProducts();
	        return ResponseEntity
	                .status(HttpStatus.OK)          // 200
	                .body(products);
	    }

	    @GetMapping("/filter")
	    public ResponseEntity<?> filterByCategory(
	            @RequestParam(value = "category", required = true) String category) {

	        List<Product> result = productService.getByCategory(category);

	        if (result.isEmpty()) {
	            return ResponseEntity
	                    .status(HttpStatus.NOT_FOUND)   // 404
	                    .body(errorResponse("No products found in category: " + category));
	        }

	        return ResponseEntity
	                .status(HttpStatus.OK)              // 200
	                .body(result);
	    }

	  
	    @GetMapping("/search")
	    public ResponseEntity<?> searchByName(
	            @RequestParam(value = "keyword", required = true) String keyword) {

	        List<Product> result = productService.searchByName(keyword);

	        if (result.isEmpty()) {
	            return ResponseEntity
	                    .status(HttpStatus.NOT_FOUND)   // 404
	                    .body(errorResponse("No products found matching keyword: " + keyword));
	        }

	        return ResponseEntity
	                .status(HttpStatus.OK)              // 200
	                .body(result);
	    }

	    @GetMapping("/{id}")
	    public ResponseEntity<?> getProductById(@PathVariable int id) {

	        Optional<Product> product = productService.getProductById(id);

	        if (product.isPresent()) {
	            return ResponseEntity
	                    .status(HttpStatus.OK)          // 200
	                    .body(product.get());
	        }

	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)       // 404
	                .body(errorResponse("Product with ID " + id + " not found."));
	    }

	    // -------------------------------------------------------
	    // 5. @RequestBody - Add new product
	    //    POST /api/products
	    //    ResponseEntity with 201 CREATED or 400 BAD REQUEST
	    // -------------------------------------------------------
	    @PostMapping
	    public ResponseEntity<?> addProduct(@RequestBody Product product) {

	        if (product.getName() == null || product.getName().isEmpty()) {
	            return ResponseEntity
	                    .status(HttpStatus.BAD_REQUEST)  // 400
	                    .body(errorResponse("Product name is required."));
	        }

	        if (product.getPrice() <= 0) {
	            return ResponseEntity
	                    .status(HttpStatus.BAD_REQUEST)  // 400
	                    .body(errorResponse("Price must be greater than 0."));
	        }

	        Product created = productService.addProduct(product);
	        return ResponseEntity
	                .status(HttpStatus.CREATED)          // 201
	                .body(created);
	    }

	    @PutMapping("/{id}")
	    public ResponseEntity<?> updateProduct(
	            @PathVariable int id,
	            @RequestBody Product updatedProduct) {

	        Product product = productService.updateProduct(id, updatedProduct);

	        if (product != null) {
	            return ResponseEntity
	                    .status(HttpStatus.OK)           // 200
	                    .body(product);
	        }

	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)        // 404
	                .body(errorResponse("Product with ID " + id + " not found. Cannot update."));
	    }

	    // -------------------------------------------------------
	    // 7. @PathVariable - Delete product
	    //    DELETE /api/products/1
	    //    ResponseEntity with 200 OK or 404 NOT FOUND
	    // -------------------------------------------------------
	    @DeleteMapping("/{id}")
	    public ResponseEntity<?> deleteProduct(@PathVariable int id) {

	        boolean deleted = productService.deleteProduct(id);

	        if (deleted) {
	            Map<String, String> response = new HashMap<>();
	            response.put("message", "Product with ID " + id + " deleted successfully.");
	            return ResponseEntity
	                    .status(HttpStatus.OK)           // 200
	                    .body(response);
	        }

	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)        // 404
	                .body(errorResponse("Product with ID " + id + " not found. Cannot delete."));
	    }

	    private Map<String, String> errorResponse(String message) {
	        Map<String, String> error = new HashMap<>();
	        error.put("error", message);
	        return error;
	    }
	}

package com.gabriel.controller;

import com.gabriel.model.Category;
import com.gabriel.model.Product;
import com.gabriel.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * CategoryController
 * Provides category endpoints - categories are extracted from products
 */
@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class CategoryController {

    @Autowired
    private ProductService productService;

    /**
     * GET /api/category - Get all categories
     * Categories are derived from unique categoryName values in products
     */
    @GetMapping("/api/category")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Product> products = productService.getAllProducts();
            Set<String> categoryNames = new HashSet<>();
            
            for (Product product : products) {
                if (product.getCategoryName() != null && !product.getCategoryName().isEmpty()) {
                    categoryNames.add(product.getCategoryName());
                }
            }
            
            List<Category> categories = new ArrayList<>();
            int id = 1;
            for (String categoryName : categoryNames) {
                Category category = new Category();
                category.setId(id++);
                category.setName(categoryName);
                category.setDescription("Products in " + categoryName + " category");
                categories.add(category);
            }
            
            return ResponseEntity.ok(categories);
        } catch (Exception ex) {
            log.error("Failed to retrieve categories: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * GET /api/category/{id} - Get category by ID
     * Note: Since categories are derived from products, ID is sequential from GET all
     */
    @GetMapping("/api/category/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Integer id) {
        try {
            List<Product> products = productService.getAllProducts();
            Set<String> categoryNames = new HashSet<>();
            
            for (Product product : products) {
                if (product.getCategoryName() != null && !product.getCategoryName().isEmpty()) {
                    categoryNames.add(product.getCategoryName());
                }
            }
            
            List<String> categoryList = new ArrayList<>(categoryNames);
            if (id > 0 && id <= categoryList.size()) {
                Category category = new Category();
                category.setId(id);
                category.setName(categoryList.get(id - 1));
                category.setDescription("Products in " + categoryList.get(id - 1) + " category");
                return ResponseEntity.ok(category);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
            }
        } catch (Exception ex) {
            log.error("Failed to retrieve category {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * PUT /api/category - Add category (BaseHttpService.add() uses PUT)
     * Note: Categories are typically derived from products, but this allows manual creation
     */
    @PutMapping("/api/category")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        try {
            // Since categories are derived from products, we just return the category
            // In a real scenario, you might want to store categories separately
            log.info("Category creation requested: {}", category.toString());
            return ResponseEntity.ok(category);
        } catch (Exception ex) {
            log.error("Failed to add category: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /**
     * POST /api/category - Update category (BaseHttpService.update() uses POST)
     */
    @PostMapping("/api/category")
    public ResponseEntity<?> updateCategory(@RequestBody Category category) {
        try {
            log.info("Category update requested: {}", category.toString());
            return ResponseEntity.ok(category);
        } catch (Exception ex) {
            log.error("Failed to update category: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}

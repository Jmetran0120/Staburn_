package com.gabriel.controller;

import com.gabriel.model.Product;
import com.gabriel.model.ProductCategory;
import com.gabriel.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/api/product")
    public ResponseEntity<?>  getProductCategories()
    {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<?> response;
        try {
            List<ProductCategory> mappedProducts = productService.listProductCategories();
            //Map<String,List<Product>> mappedProducts = productService.getCategoryMappedProducts();
            log.warn("Product Categories Count:::::::" + mappedProducts.size());
            response = ResponseEntity.ok(mappedProducts);
        }
        catch( Exception ex)
        {
            log.error("Failed to retrieve product with id : {}", ex.getMessage(), ex);
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return response;
    }

    @PutMapping("/api/product")
    public ResponseEntity<?> add(@RequestBody Product product){
        log.info("Input >> " + product.toString() );
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<?> response;
        try {
            Product newProduct = productService.create(product);
            log.info("created product >> " + newProduct.toString() );
            response = ResponseEntity.ok(newProduct);
        }
        catch( Exception ex)
        {
            log.error("Failed to retrieve product with id : {}", ex.getMessage(), ex);
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return response;
    }
    @PostMapping("/api/product")
    public ResponseEntity<?> update(@RequestBody Product product){
        log.info("Update Input >> product.toString() ");
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<?> response;
        try {
            Product newProduct = productService.update(product);
            response = ResponseEntity.ok(product);
        }
        catch( Exception ex)
        {
            log.error("Failed to retrieve product with id : {}", ex.getMessage(), ex);
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return response;
    }

    @GetMapping("/api/product/{id}")
    public ResponseEntity<?> get(@PathVariable final Integer id){
        log.info("Input product id >> " + Integer.toString(id));
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<?> response;
        try {
            Product product = productService.get(id);
            response = ResponseEntity.ok(product);
        }
        catch( Exception ex)
        {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return response;
    }
    @DeleteMapping("/api/product/{id}")
    public ResponseEntity<?> delete(@PathVariable final Integer id){
        log.info("Input >> " + Integer.toString(id));
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<?> response;
        try {
            productService.delete(id);
            response = ResponseEntity.ok(null);
        }
        catch( Exception ex)
        {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        return response;
    }
    
    // Vehicle-specific endpoints
    // Note: Using listProductCategories() which works with existing schema
    // Once Hibernate updates the schema with new vehicle columns, filtering will be enabled
    @GetMapping("/api/inventory")
    public ResponseEntity<?> getInventory(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer yearMin,
            @RequestParam(required = false) Integer yearMax,
            @RequestParam(required = false) Double priceMin,
            @RequestParam(required = false) Double priceMax,
            @RequestParam(required = false) Integer mileageMin,
            @RequestParam(required = false) Integer mileageMax,
            @RequestParam(required = false) String fuelType,
            @RequestParam(required = false) String transmission,
            @RequestParam(required = false) String status) {
        try {
            // Return products grouped by category - frontend can flatten and filter
            List<ProductCategory> categories = productService.listProductCategories();
            // Flatten into single list for inventory view
            List<Product> vehicles = new ArrayList<>();
            for (ProductCategory category : categories) {
                if (category.getProducts() != null) {
                    vehicles.addAll(category.getProducts());
                }
            }
            return ResponseEntity.ok(vehicles);
        } catch (Exception ex) {
            log.error("Failed to retrieve inventory: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    
}

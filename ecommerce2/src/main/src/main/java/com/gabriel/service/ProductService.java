package com.gabriel.service;

import com.gabriel.model.Product;
import com.gabriel.model.ProductCategory;

import java.util.*;

public interface ProductService {

    List<Product> getAllProducts();
    Product[] getAll();
    Product get(Integer id);
    Product create(Product product);
    Product update(Product product);
    void delete(Integer id);
    Map<String, List<Product>> getCategoryMappedProducts();
    List<ProductCategory> listProductCategories();
    
    // Vehicle-specific methods
    List<Product> getVehiclesWithFilters(
        String make, String model, Integer yearMin, Integer yearMax,
        Double priceMin, Double priceMax, Integer mileageMin, Integer mileageMax,
        String fuelType, String transmission, String status
    );
}

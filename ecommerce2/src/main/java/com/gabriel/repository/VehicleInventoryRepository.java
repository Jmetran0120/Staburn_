package com.gabriel.repository;

import com.gabriel.entity.VehicleInventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleInventoryRepository extends CrudRepository<VehicleInventory, Integer> {
    
    // Find vehicles by condition (New/Used)
    List<VehicleInventory> findByConditionAndInStock(String condition, Boolean inStock);
    
    // Find vehicles by make
    List<VehicleInventory> findByMakeAndInStock(String make, Boolean inStock);
    
    // Find vehicles by condition
    List<VehicleInventory> findByCondition(String condition);
    
    // Find featured vehicles
    List<VehicleInventory> findByFeaturedAndInStock(Boolean featured, Boolean inStock);
    
    // Find vehicles by make and model
    List<VehicleInventory> findByMakeAndModelAndInStock(String make, String model, Boolean inStock);
    
    // Find all vehicles in stock
    List<VehicleInventory> findByInStock(Boolean inStock);
    
    // Get distinct makes
    @Query("SELECT DISTINCT v.make FROM VehicleInventory v WHERE v.inStock = true ORDER BY v.make")
    List<String> findDistinctMakes();
    
    // Get distinct models by make
    @Query("SELECT DISTINCT v.model FROM VehicleInventory v WHERE v.make = :make AND v.inStock = true ORDER BY v.model")
    List<String> findDistinctModelsByMake(@Param("make") String make);
    
    // Search vehicles with multiple filters
    @Query(value = "SELECT * FROM vehicle_inventory WHERE " +
           "in_stock = true AND " +
           "(:make IS NULL OR make = :make) AND " +
           "(:model IS NULL OR model = :model) AND " +
           "(:yearMin IS NULL OR year >= :yearMin) AND " +
           "(:yearMax IS NULL OR year <= :yearMax) AND " +
           "(:priceMin IS NULL OR price >= :priceMin) AND " +
           "(:priceMax IS NULL OR price <= :priceMax) AND " +
           "(:mileageMax IS NULL OR mileage <= :mileageMax) AND " +
           "(:fuelType IS NULL OR fuel_type = :fuelType) AND " +
           "(:transmission IS NULL OR transmission = :transmission) AND " +
           "(:condition IS NULL OR `condition` = :condition) " +
           "ORDER BY featured DESC, year DESC, price ASC", nativeQuery = true)
    List<VehicleInventory> searchVehiclesWithFilters(
        @Param("make") String make,
        @Param("model") String model,
        @Param("yearMin") Integer yearMin,
        @Param("yearMax") Integer yearMax,
        @Param("priceMin") Double priceMin,
        @Param("priceMax") Double priceMax,
        @Param("mileageMax") Integer mileageMax,
        @Param("fuelType") String fuelType,
        @Param("transmission") String transmission,
        @Param("condition") String condition
    );
}


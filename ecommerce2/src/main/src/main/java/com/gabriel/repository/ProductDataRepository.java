package com.gabriel.repository;

import com.gabriel.entity.ProductData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDataRepository extends CrudRepository<ProductData, Integer> {
    
    // Filtering methods for vehicle catalog
    List<ProductData> findByMake(String make);
    List<ProductData> findByModel(String model);
    List<ProductData> findByYear(Integer year);
    List<ProductData> findByFuelType(String fuelType);
    List<ProductData> findByTransmission(String transmission);
    List<ProductData> findByStatus(String status);
    
    // Combined filtering - simplified to work with existing schema
    // Note: Price filtering will be done in service layer since price is stored as String
    List<ProductData> findAll();
    
    @Query("SELECT p FROM ProductData p WHERE " +
           "(:make IS NULL OR p.make = :make) AND " +
           "(:model IS NULL OR p.model = :model) AND " +
           "(:yearMin IS NULL OR p.year >= :yearMin) AND " +
           "(:yearMax IS NULL OR p.year <= :yearMax) AND " +
           "(:mileageMin IS NULL OR p.mileage >= :mileageMin) AND " +
           "(:mileageMax IS NULL OR p.mileage <= :mileageMax) AND " +
           "(:fuelType IS NULL OR p.fuelType = :fuelType) AND " +
           "(:transmission IS NULL OR p.transmission = :transmission) AND " +
           "(:status IS NULL OR p.status = :status)")
    List<ProductData> findVehiclesWithFilters(
        @Param("make") String make,
        @Param("model") String model,
        @Param("yearMin") Integer yearMin,
        @Param("yearMax") Integer yearMax,
        @Param("mileageMin") Integer mileageMin,
        @Param("mileageMax") Integer mileageMax,
        @Param("fuelType") String fuelType,
        @Param("transmission") String transmission,
        @Param("status") String status
    );
}

package com.gabriel.model;

import lombok.Data;

@Data
public class Product {
    int id;
    String name;
    String description;
    String categoryName;
    String imageFile;
    String unitOfMeasure;
    String price;
    
    // Vehicle-specific fields
    String make;
    String model;
    Integer year;
    Integer mileage;
    String fuelType;
    String transmission;
    String vin;
    String condition;
    String exteriorColor;
    String interiorColor;
    String drivetrain;
    Integer horsepower;
    Integer mpgCity;
    Integer mpgHighway;
    String bodyStyle;
    Integer seating;
    String status;
}

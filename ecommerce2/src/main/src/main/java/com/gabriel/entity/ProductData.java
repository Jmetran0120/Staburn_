package com.gabriel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "product_data")
public class ProductData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String name;
    String description;
    String categoryName;
    String unitOfMeasure;
    String price;
    String imageFile;
    
    // Vehicle-specific fields
    String make;           // e.g., "Toyota", "Honda"
    String model;          // e.g., "Camry", "Civic"
    Integer year;          // e.g., 2023
    Integer mileage;       // e.g., 15000
    String fuelType;      // e.g., "Gasoline", "Electric", "Hybrid", "Diesel"
    String transmission;  // e.g., "Automatic", "Manual", "CVT"
    String vin;           // Vehicle Identification Number
    @Column(name = "condition")
    String condition;     // e.g., "New", "Used", "Certified Pre-Owned"
    String exteriorColor;
    String interiorColor;
    String drivetrain;     // e.g., "FWD", "RWD", "AWD", "4WD"
    Integer horsepower;
    Integer mpgCity;
    Integer mpgHighway;
    String bodyStyle;      // e.g., "Sedan", "SUV", "Truck", "Coupe"
    Integer seating;
    String status;         // e.g., "Available", "Sold", "Pending"

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date lastUpdated;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date created;
}

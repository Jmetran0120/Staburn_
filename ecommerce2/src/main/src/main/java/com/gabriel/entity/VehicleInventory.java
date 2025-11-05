package com.gabriel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "vehicle_inventory", 
       indexes = {
           @Index(name = "idx_make", columnList = "make"),
           @Index(name = "idx_model", columnList = "model"),
           @Index(name = "idx_year", columnList = "year"),
           @Index(name = "idx_condition", columnList = "condition"),
           @Index(name = "idx_price", columnList = "price"),
           @Index(name = "idx_featured", columnList = "featured"),
           @Index(name = "idx_in_stock", columnList = "in_stock"),
           @Index(name = "idx_make_model", columnList = "make,model"),
           @Index(name = "idx_condition_featured", columnList = "condition,featured")
       })
public class VehicleInventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "vin", unique = true, nullable = false, length = 17)
    private String vin;
    
    @Column(name = "make", nullable = false, length = 50)
    private String make;
    
    @Column(name = "model", nullable = false, length = 100)
    private String model;
    
    @Column(name = "trim", length = 100)
    private String trim;
    
    @Column(name = "year", nullable = false)
    private Integer year;
    
    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private Double price;
    
    @Column(name = "mileage", nullable = false)
    private Integer mileage = 0;
    
    @Column(name = "fuel_type", nullable = false, length = 50)
    @JsonProperty("fuelType")
    private String fuelType;
    
    @Column(name = "transmission", nullable = false, length = 50)
    private String transmission;
    
    @Column(name = "color", nullable = false, length = 50)
    private String color;
    
    @Column(name = "condition", nullable = false, length = 20)
    private String condition = "New";
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "images", columnDefinition = "TEXT")
    private String images; // JSON array stored as string, will be parsed to List<String> in DTO
    
    @Column(name = "video_url", length = 500)
    @JsonProperty("videoUrl")
    private String videoUrl;
    
    @Column(name = "three_sixty_url", length = 500)
    @JsonProperty("threeSixtyUrl")
    private String threeSixtyUrl;
    
    @Column(name = "in_stock", nullable = false)
    @JsonProperty("inStock")
    private Boolean inStock = true;
    
    @Column(name = "featured", nullable = false)
    private Boolean featured = false;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column(name = "created")
    private Date created;
    
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    @Column(name = "last_updated")
    private Date lastUpdated;
}


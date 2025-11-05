package com.gabriel.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.entity.VehicleInventory;
import com.gabriel.repository.VehicleInventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@Slf4j
public class VehicleController {

    @Autowired
    private VehicleInventoryRepository vehicleInventoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Helper method to convert VehicleInventory to Map (for JSON response matching frontend model)
    private Map<String, Object> vehicleToMap(VehicleInventory vehicle) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", vehicle.getId());
        map.put("vin", vehicle.getVin());
        map.put("make", vehicle.getMake());
        map.put("model", vehicle.getModel());
        map.put("trim", vehicle.getTrim());
        map.put("year", vehicle.getYear());
        map.put("price", vehicle.getPrice());
        map.put("mileage", vehicle.getMileage());
        map.put("fuelType", vehicle.getFuelType());
        map.put("transmission", vehicle.getTransmission());
        map.put("color", vehicle.getColor());
        map.put("condition", vehicle.getCondition());
        map.put("description", vehicle.getDescription());
        
        // Parse images JSON string to array
        try {
            if (vehicle.getImages() != null && !vehicle.getImages().isEmpty()) {
                List<String> imagesList = objectMapper.readValue(vehicle.getImages(), new TypeReference<List<String>>() {});
                map.put("images", imagesList);
            } else {
                map.put("images", new ArrayList<String>());
            }
        } catch (Exception e) {
            log.warn("Failed to parse images JSON for vehicle {}: {}", vehicle.getId(), e.getMessage());
            map.put("images", new ArrayList<String>());
        }
        
        map.put("videoUrl", vehicle.getVideoUrl());
        map.put("threeSixtyUrl", vehicle.getThreeSixtyUrl());
        map.put("inStock", vehicle.getInStock());
        map.put("featured", vehicle.getFeatured());
        return map;
    }

    // GET all vehicles
    @GetMapping("/api/vehicle")
    public ResponseEntity<?> getAllVehicles() {
        try {
            List<VehicleInventory> vehicles = vehicleInventoryRepository.findByInStock(true);
            List<Map<String, Object>> result = vehicles.stream()
                    .map(this::vehicleToMap)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Failed to retrieve vehicles: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET vehicle by ID
    @GetMapping("/api/vehicle/{id}")
    public ResponseEntity<?> getVehicleById(@PathVariable Integer id) {
        try {
            Optional<VehicleInventory> vehicleOpt = vehicleInventoryRepository.findById(id);
            if (vehicleOpt.isPresent()) {
                return ResponseEntity.ok(vehicleToMap(vehicleOpt.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
            }
        } catch (Exception ex) {
            log.error("Failed to retrieve vehicle {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET new vehicles
    @GetMapping("/api/vehicle/new")
    public ResponseEntity<?> getNewVehicles() {
        try {
            List<VehicleInventory> vehicles = vehicleInventoryRepository.findByConditionAndInStock("New", true);
            List<Map<String, Object>> result = vehicles.stream()
                    .map(this::vehicleToMap)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Failed to retrieve new vehicles: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET used vehicles
    @GetMapping("/api/vehicle/used")
    public ResponseEntity<?> getUsedVehicles() {
        try {
            List<VehicleInventory> vehicles = vehicleInventoryRepository.findByConditionAndInStock("Used", true);
            List<Map<String, Object>> result = vehicles.stream()
                    .map(this::vehicleToMap)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Failed to retrieve used vehicles: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET vehicles by make
    @GetMapping("/api/vehicle/make/{make}")
    public ResponseEntity<?> getVehiclesByMake(@PathVariable String make) {
        try {
            List<VehicleInventory> vehicles = vehicleInventoryRepository.findByMakeAndInStock(make, true);
            List<Map<String, Object>> result = vehicles.stream()
                    .map(this::vehicleToMap)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Failed to retrieve vehicles by make {}: {}", make, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET featured vehicles
    @GetMapping("/api/vehicle/featured")
    public ResponseEntity<?> getFeaturedVehicles() {
        try {
            List<VehicleInventory> vehicles = vehicleInventoryRepository.findByFeaturedAndInStock(true, true);
            List<Map<String, Object>> result = vehicles.stream()
                    .map(this::vehicleToMap)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Failed to retrieve featured vehicles: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // POST search vehicles with filters
    @PostMapping("/api/vehicle/search")
    public ResponseEntity<?> searchVehicles(@RequestBody Map<String, Object> filterMap) {
        try {
            String make = filterMap.containsKey("make") && filterMap.get("make") != null 
                    ? filterMap.get("make").toString() : null;
            String model = filterMap.containsKey("model") && filterMap.get("model") != null 
                    ? filterMap.get("model").toString() : null;
            Integer yearMin = filterMap.containsKey("yearMin") && filterMap.get("yearMin") != null 
                    ? Integer.valueOf(filterMap.get("yearMin").toString()) : null;
            Integer yearMax = filterMap.containsKey("yearMax") && filterMap.get("yearMax") != null 
                    ? Integer.valueOf(filterMap.get("yearMax").toString()) : null;
            Double priceMin = filterMap.containsKey("priceMin") && filterMap.get("priceMin") != null 
                    ? Double.valueOf(filterMap.get("priceMin").toString()) : null;
            Double priceMax = filterMap.containsKey("priceMax") && filterMap.get("priceMax") != null 
                    ? Double.valueOf(filterMap.get("priceMax").toString()) : null;
            Integer mileageMax = filterMap.containsKey("mileageMax") && filterMap.get("mileageMax") != null 
                    ? Integer.valueOf(filterMap.get("mileageMax").toString()) : null;
            String fuelType = filterMap.containsKey("fuelType") && filterMap.get("fuelType") != null 
                    ? filterMap.get("fuelType").toString() : null;
            String transmission = filterMap.containsKey("transmission") && filterMap.get("transmission") != null 
                    ? filterMap.get("transmission").toString() : null;
            String condition = filterMap.containsKey("condition") && filterMap.get("condition") != null 
                    ? filterMap.get("condition").toString() : null;

            List<VehicleInventory> vehicles = vehicleInventoryRepository.searchVehiclesWithFilters(
                    make, model, yearMin, yearMax, priceMin, priceMax, mileageMax, fuelType, transmission, condition);
            
            List<Map<String, Object>> result = vehicles.stream()
                    .map(this::vehicleToMap)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Failed to search vehicles: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // POST create vehicle (for admin use)
    @PostMapping("/api/vehicle")
    public ResponseEntity<?> createVehicle(@RequestBody Map<String, Object> vehicleMap) {
        try {
            VehicleInventory vehicle = new VehicleInventory();
            
            vehicle.setVin(vehicleMap.get("vin").toString());
            vehicle.setMake(vehicleMap.get("make").toString());
            vehicle.setModel(vehicleMap.get("model").toString());
            vehicle.setTrim(vehicleMap.containsKey("trim") && vehicleMap.get("trim") != null 
                    ? vehicleMap.get("trim").toString() : null);
            vehicle.setYear(Integer.valueOf(vehicleMap.get("year").toString()));
            vehicle.setPrice(Double.valueOf(vehicleMap.get("price").toString()));
            vehicle.setMileage(vehicleMap.containsKey("mileage") && vehicleMap.get("mileage") != null 
                    ? Integer.valueOf(vehicleMap.get("mileage").toString()) : 0);
            vehicle.setFuelType(vehicleMap.get("fuelType").toString());
            vehicle.setTransmission(vehicleMap.get("transmission").toString());
            vehicle.setColor(vehicleMap.get("color").toString());
            vehicle.setCondition(vehicleMap.containsKey("condition") && vehicleMap.get("condition") != null 
                    ? vehicleMap.get("condition").toString() : "New");
            vehicle.setDescription(vehicleMap.containsKey("description") && vehicleMap.get("description") != null 
                    ? vehicleMap.get("description").toString() : null);
            
            // Convert images array to JSON string
            if (vehicleMap.containsKey("images") && vehicleMap.get("images") != null) {
                try {
                    String imagesJson = objectMapper.writeValueAsString(vehicleMap.get("images"));
                    vehicle.setImages(imagesJson);
                } catch (Exception e) {
                    log.warn("Failed to convert images to JSON: {}", e.getMessage());
                    vehicle.setImages("[]");
                }
            } else {
                vehicle.setImages("[]");
            }
            
            vehicle.setVideoUrl(vehicleMap.containsKey("videoUrl") && vehicleMap.get("videoUrl") != null 
                    ? vehicleMap.get("videoUrl").toString() : null);
            vehicle.setThreeSixtyUrl(vehicleMap.containsKey("threeSixtyUrl") && vehicleMap.get("threeSixtyUrl") != null 
                    ? vehicleMap.get("threeSixtyUrl").toString() : null);
            vehicle.setInStock(vehicleMap.containsKey("inStock") && vehicleMap.get("inStock") != null 
                    ? Boolean.valueOf(vehicleMap.get("inStock").toString()) : true);
            vehicle.setFeatured(vehicleMap.containsKey("featured") && vehicleMap.get("featured") != null 
                    ? Boolean.valueOf(vehicleMap.get("featured").toString()) : false);

            VehicleInventory savedVehicle = vehicleInventoryRepository.save(vehicle);
            log.info("Created vehicle: {} - {} {}", savedVehicle.getId(), savedVehicle.getMake(), savedVehicle.getModel());
            return ResponseEntity.ok(vehicleToMap(savedVehicle));
        } catch (Exception ex) {
            log.error("Failed to create vehicle: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // PUT update vehicle (for admin use)
    @PutMapping("/api/vehicle/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable Integer id, @RequestBody Map<String, Object> vehicleMap) {
        try {
            Optional<VehicleInventory> vehicleOpt = vehicleInventoryRepository.findById(id);
            if (!vehicleOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
            }

            VehicleInventory vehicle = vehicleOpt.get();
            
            if (vehicleMap.containsKey("vin")) vehicle.setVin(vehicleMap.get("vin").toString());
            if (vehicleMap.containsKey("make")) vehicle.setMake(vehicleMap.get("make").toString());
            if (vehicleMap.containsKey("model")) vehicle.setModel(vehicleMap.get("model").toString());
            if (vehicleMap.containsKey("trim")) vehicle.setTrim(vehicleMap.get("trim") != null ? vehicleMap.get("trim").toString() : null);
            if (vehicleMap.containsKey("year")) vehicle.setYear(Integer.valueOf(vehicleMap.get("year").toString()));
            if (vehicleMap.containsKey("price")) vehicle.setPrice(Double.valueOf(vehicleMap.get("price").toString()));
            if (vehicleMap.containsKey("mileage")) vehicle.setMileage(Integer.valueOf(vehicleMap.get("mileage").toString()));
            if (vehicleMap.containsKey("fuelType")) vehicle.setFuelType(vehicleMap.get("fuelType").toString());
            if (vehicleMap.containsKey("transmission")) vehicle.setTransmission(vehicleMap.get("transmission").toString());
            if (vehicleMap.containsKey("color")) vehicle.setColor(vehicleMap.get("color").toString());
            if (vehicleMap.containsKey("condition")) vehicle.setCondition(vehicleMap.get("condition").toString());
            if (vehicleMap.containsKey("description")) vehicle.setDescription(vehicleMap.get("description") != null ? vehicleMap.get("description").toString() : null);
            
            if (vehicleMap.containsKey("images")) {
                try {
                    String imagesJson = objectMapper.writeValueAsString(vehicleMap.get("images"));
                    vehicle.setImages(imagesJson);
                } catch (Exception e) {
                    log.warn("Failed to convert images to JSON: {}", e.getMessage());
                }
            }
            
            if (vehicleMap.containsKey("videoUrl")) vehicle.setVideoUrl(vehicleMap.get("videoUrl") != null ? vehicleMap.get("videoUrl").toString() : null);
            if (vehicleMap.containsKey("threeSixtyUrl")) vehicle.setThreeSixtyUrl(vehicleMap.get("threeSixtyUrl") != null ? vehicleMap.get("threeSixtyUrl").toString() : null);
            if (vehicleMap.containsKey("inStock")) vehicle.setInStock(Boolean.valueOf(vehicleMap.get("inStock").toString()));
            if (vehicleMap.containsKey("featured")) vehicle.setFeatured(Boolean.valueOf(vehicleMap.get("featured").toString()));

            VehicleInventory savedVehicle = vehicleInventoryRepository.save(vehicle);
            log.info("Updated vehicle: {} - {} {}", savedVehicle.getId(), savedVehicle.getMake(), savedVehicle.getModel());
            return ResponseEntity.ok(vehicleToMap(savedVehicle));
        } catch (Exception ex) {
            log.error("Failed to update vehicle {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // DELETE vehicle (for admin use)
    @DeleteMapping("/api/vehicle/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Integer id) {
        try {
            if (!vehicleInventoryRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found");
            }
            vehicleInventoryRepository.deleteById(id);
            log.info("Deleted vehicle: {}", id);
            return ResponseEntity.ok("Vehicle deleted successfully");
        } catch (Exception ex) {
            log.error("Failed to delete vehicle {}: {}", id, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET available makes (for filter dropdown)
    @GetMapping("/api/vehicle/makes")
    public ResponseEntity<?> getAvailableMakes() {
        try {
            List<String> makes = vehicleInventoryRepository.findDistinctMakes();
            return ResponseEntity.ok(makes);
        } catch (Exception ex) {
            log.error("Failed to retrieve makes: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET models by make (for filter dropdown)
    @GetMapping("/api/vehicle/models/{make}")
    public ResponseEntity<?> getModelsByMake(@PathVariable String make) {
        try {
            List<String> models = vehicleInventoryRepository.findDistinctModelsByMake(make);
            return ResponseEntity.ok(models);
        } catch (Exception ex) {
            log.error("Failed to retrieve models for make {}: {}", make, ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // GET inventory status (check if inventory is empty)
    @GetMapping(value = "/api/vehicle/status", produces = "application/json")
    public ResponseEntity<?> getInventoryStatus() {
        try {
            List<VehicleInventory> allVehicles = (List<VehicleInventory>) vehicleInventoryRepository.findAll();
            long totalVehicles = allVehicles.size();
            long vehiclesInStock = allVehicles.stream().filter(v -> v.getInStock() != null && v.getInStock()).count();
            long newVehicles = allVehicles.stream()
                    .filter(v -> "New".equals(v.getCondition()) && v.getInStock() != null && v.getInStock())
                    .count();
            long usedVehicles = allVehicles.stream()
                    .filter(v -> "Used".equals(v.getCondition()) && v.getInStock() != null && v.getInStock())
                    .count();
            long featuredVehicles = allVehicles.stream()
                    .filter(v -> v.getFeatured() != null && v.getFeatured() && v.getInStock() != null && v.getInStock())
                    .count();

            String status;
            if (totalVehicles == 0) {
                status = "EMPTY";
            } else if (vehiclesInStock == 0) {
                status = "NO_STOCK";
            } else {
                status = "HAS_STOCK";
            }

            Map<String, Object> statusMap = new HashMap<>();
            statusMap.put("totalVehicles", totalVehicles);
            statusMap.put("vehiclesInStock", vehiclesInStock);
            statusMap.put("newVehicles", newVehicles);
            statusMap.put("usedVehicles", usedVehicles);
            statusMap.put("featuredVehicles", featuredVehicles);
            statusMap.put("status", status);
            statusMap.put("isEmpty", totalVehicles == 0);
            statusMap.put("hasStock", vehiclesInStock > 0);

            return ResponseEntity.ok(statusMap);
        } catch (Exception ex) {
            log.error("Failed to get inventory status: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}


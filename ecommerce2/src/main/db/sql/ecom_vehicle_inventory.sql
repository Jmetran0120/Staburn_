-- =====================================================
-- Vehicle Inventory SQL Script - Automotive Website
-- This script creates the vehicle inventory table to store
-- all available vehicles (new and used) in the database
-- =====================================================

-- =====================================================
-- Create Vehicle Inventory Table
-- =====================================================

CREATE TABLE IF NOT EXISTS vehicle_inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vin VARCHAR(17) UNIQUE NOT NULL COMMENT 'Vehicle Identification Number',
    make VARCHAR(50) NOT NULL COMMENT 'Vehicle manufacturer (Ford, Toyota, Honda, etc.)',
    model VARCHAR(100) NOT NULL COMMENT 'Vehicle model name',
    trim VARCHAR(100) DEFAULT NULL COMMENT 'Trim level (e.g., "EX-L", "Limited")',
    year INT NOT NULL COMMENT 'Model year',
    price DECIMAL(12, 2) NOT NULL COMMENT 'Vehicle price in PHP',
    mileage INT DEFAULT 0 COMMENT 'Mileage (0 for new vehicles)',
    fuel_type VARCHAR(50) NOT NULL COMMENT 'Fuel type (Diesel, Gasoline, Electric, Hybrid)',
    transmission VARCHAR(50) NOT NULL COMMENT 'Transmission type (Automatic, Manual, CVT)',
    color VARCHAR(50) NOT NULL COMMENT 'Vehicle color',
    condition VARCHAR(20) NOT NULL DEFAULT 'New' COMMENT 'Condition: New or Used',
    description TEXT COMMENT 'Detailed vehicle description',
    images TEXT COMMENT 'Array of image paths (stored as JSON string)',
    video_url VARCHAR(500) DEFAULT NULL COMMENT 'Optional video URL',
    three_sixty_url VARCHAR(500) DEFAULT NULL COMMENT 'Optional 360-degree view URL',
    in_stock BOOLEAN DEFAULT TRUE COMMENT 'Whether vehicle is currently in stock',
    featured BOOLEAN DEFAULT FALSE COMMENT 'Whether vehicle is featured/promoted',
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'When vehicle was added to inventory',
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    
    -- Indexes for common queries
    INDEX idx_make (make),
    INDEX idx_model (model),
    INDEX idx_year (year),
    INDEX idx_condition (condition),
    INDEX idx_price (price),
    INDEX idx_featured (featured),
    INDEX idx_in_stock (in_stock),
    INDEX idx_make_model (make, model),
    INDEX idx_condition_featured (condition, featured)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Vehicle inventory catalog';

-- =====================================================
-- Example: Insert New Vehicles
-- =====================================================

INSERT INTO vehicle_inventory (
    vin, make, model, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'ABC123456789',
    'Ford',
    'Everest',
    2024,
    1849000.00,
    0,
    'Diesel',
    'Automatic',
    'Grey',
    'New',
    'Ford Everest SUV - A powerful and rugged SUV perfect for adventure.',
    '["/assets/img/2024 ford everest.avif"]',
    TRUE,
    FALSE
);

INSERT INTO vehicle_inventory (
    vin, make, model, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'DEF456789012',
    'Nissan',
    'Terra',
    2024,
    1969000.00,
    0,
    'Diesel',
    'Automatic',
    'White',
    'New',
    'Nissan Terra SUV - Premium SUV with advanced features.',
    '["/assets/img/2024 nissan terra.jpg"]',
    TRUE,
    TRUE
);

INSERT INTO vehicle_inventory (
    vin, make, model, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'GHI789012345',
    'Mitsubishi',
    'Montero Sport',
    2024,
    1568000.00,
    0,
    'Diesel',
    'Automatic',
    'Silver',
    'New',
    'Mitsubishi Montero Sport - Reliable and versatile SUV.',
    '["/assets/img/2024 mitsubishi montero sport.jpg"]',
    TRUE,
    TRUE
);

-- =====================================================
-- Example: Insert Used Vehicles
-- =====================================================

INSERT INTO vehicle_inventory (
    vin, make, model, trim, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'JKL012345678',
    'Toyota',
    'Corolla Altis',
    '1.6V AT',
    2015,
    498000.00,
    80000,
    'Gasoline',
    'Automatic',
    'White',
    'Used',
    '2015 Toyota Corolla Altis - Well maintained, one owner.',
    '["/assets/img/2015 Toyota Corolla Altis.jpg"]',
    TRUE,
    FALSE
);

INSERT INTO vehicle_inventory (
    vin, make, model, trim, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'MNO345678901',
    'Toyota',
    'Hiace Commuter',
    'MT Diesel',
    2020,
    968000.00,
    87000,
    'Diesel',
    'Manual',
    'White',
    'Used',
    '2020 Toyota Hiace Commuter - Perfect for business use.',
    '["/assets/img/2020 Toyota Hiace Commuter.jpeg"]',
    TRUE,
    FALSE
);

INSERT INTO vehicle_inventory (
    vin, make, model, trim, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'PQR678901234',
    'Honda',
    'CR-V',
    2024,
    0,
    0,
    'Gasoline',
    'Automatic',
    'White',
    'New',
    'Honda CR-V - Spacious and reliable SUV with excellent fuel efficiency.',
    '["/assets/img/2024 honda cr-v.jpg"]',
    TRUE,
    TRUE
);

INSERT INTO vehicle_inventory (
    vin, make, model, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'STU901234567',
    'Mazda',
    'CX-5',
    2024,
    0,
    0,
    'Gasoline',
    'Automatic',
    'Red',
    'New',
    'Mazda CX-5 - Premium compact SUV with sporty design and advanced technology.',
    '["/assets/img/2024 mazda cx-5.jpg"]',
    TRUE,
    TRUE
);

INSERT INTO vehicle_inventory (
    vin, make, model, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'VWX234567890',
    'Toyota',
    'Fortuner',
    2024,
    0,
    0,
    'Diesel',
    'Automatic',
    'White',
    'New',
    'Toyota Fortuner - Powerful and luxurious SUV with off-road capabilities.',
    '["/assets/img/2024 toyota fortuner.jpg"]',
    TRUE,
    TRUE
);

INSERT INTO vehicle_inventory (
    vin, make, model, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'YZA567890123',
    'Toyota',
    'Wigo',
    2024,
    0,
    0,
    'Gasoline',
    'Manual',
    'White',
    'New',
    'Toyota Wigo - Compact and efficient city car perfect for urban driving.',
    '["/assets/img/2024 Toyota Wigo.jpg"]',
    TRUE,
    FALSE
);

INSERT INTO vehicle_inventory (
    vin, make, model, trim, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'BCD890123456',
    'Honda',
    'City',
    '1.5 E CVT',
    2018,
    598000.00,
    65000,
    'Gasoline',
    'CVT',
    'White',
    'Used',
    '2018 Honda City - Reliable sedan with excellent fuel economy.',
    '["/assets/img/2018 Honda City 1.5 E CVT.jpg"]',
    TRUE,
    FALSE
);

INSERT INTO vehicle_inventory (
    vin, make, model, trim, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'EFG123456789',
    'Honda',
    'Civic',
    '1.8 E CVT',
    2019,
    798000.00,
    45000,
    'Gasoline',
    'CVT',
    'White',
    'Used',
    '2019 Honda Civic - Sporty and reliable sedan with modern features.',
    '["/assets/img/2019 Honda Civic 1.8 E CVT.jpg"]',
    TRUE,
    FALSE
);

INSERT INTO vehicle_inventory (
    vin, make, model, trim, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'HIJ456789012',
    'Mitsubishi',
    'Mirage G4',
    'GLX CVT',
    2019,
    398000.00,
    55000,
    'Gasoline',
    'CVT',
    'White',
    'Used',
    '2019 Mitsubishi Mirage G4 - Fuel-efficient compact sedan.',
    '["/assets/img/2019 Mitsubishi Mirage G4 GLX CVT.webp"]',
    TRUE,
    FALSE
);

INSERT INTO vehicle_inventory (
    vin, make, model, trim, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'KLM789012345',
    'Mitsubishi',
    'Montero Sport',
    'GLS AT',
    2020,
    1198000.00,
    35000,
    'Diesel',
    'Automatic',
    'White',
    'Used',
    '2020 Mitsubishi Montero Sport - Premium SUV with low mileage.',
    '["/assets/img/2020 Mitsubishi Montero Sport GLS AT.jpg"]',
    TRUE,
    TRUE
);

INSERT INTO vehicle_inventory (
    vin, make, model, trim, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'NOP012345678',
    'Nissan',
    'Almera',
    'VL CVT',
    2020,
    548000.00,
    50000,
    'Gasoline',
    'CVT',
    'White',
    'Used',
    '2020 Nissan Almera - Compact sedan with modern features.',
    '["/assets/img/2020 Nissan Almera VL CVT.jpg"]',
    TRUE,
    FALSE
);

INSERT INTO vehicle_inventory (
    vin, make, model, trim, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'QRS345678901',
    'Nissan',
    'Terra',
    'VL 4x2 AT',
    2021,
    1468000.00,
    25000,
    'Diesel',
    'Automatic',
    'White',
    'Used',
    '2021 Nissan Terra - Premium SUV with low mileage and excellent condition.',
    '["/assets/img/2021 Nissan Terra VL 4x2 AT.webp"]',
    TRUE,
    TRUE
);

INSERT INTO vehicle_inventory (
    vin, make, model, trim, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'TUV678901234',
    'Subaru',
    'XV',
    '2.0i-S',
    2018,
    898000.00,
    60000,
    'Gasoline',
    'Automatic',
    'Blue',
    'Used',
    '2018 Subaru XV - Compact crossover with all-wheel drive capability.',
    '["/assets/img/2018 Subaru XV 2.0i-S"]',
    TRUE,
    FALSE
);

INSERT INTO vehicle_inventory (
    vin, make, model, trim, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'WXY901234567',
    'Subaru',
    'Forester',
    '2.0i-S',
    2019,
    1298000.00,
    40000,
    'Gasoline',
    'Automatic',
    'White',
    'Used',
    '2019 Subaru Forester - Spacious SUV with all-wheel drive and safety features.',
    '["/assets/img/2019 Subaru Forester 2.0i-S"]',
    TRUE,
    TRUE
);

-- =====================================================
-- Query: Get All New Vehicles
-- =====================================================

SELECT 
    id,
    vin,
    make,
    model,
    trim,
    year,
    price,
    mileage,
    fuel_type,
    transmission,
    color,
    condition,
    description,
    images,
    video_url,
    three_sixty_url,
    in_stock,
    featured
FROM vehicle_inventory
WHERE condition = 'New'
  AND in_stock = TRUE
ORDER BY featured DESC, year DESC, make, model;

-- =====================================================
-- Query: Get All Used Vehicles
-- =====================================================

SELECT 
    id,
    vin,
    make,
    model,
    trim,
    year,
    price,
    mileage,
    fuel_type,
    transmission,
    color,
    condition,
    description,
    images,
    video_url,
    three_sixty_url,
    in_stock,
    featured
FROM vehicle_inventory
WHERE condition = 'Used'
  AND in_stock = TRUE
ORDER BY year DESC, price ASC;

-- =====================================================
-- Query: Get Vehicle by ID
-- =====================================================

SELECT 
    id,
    vin,
    make,
    model,
    trim,
    year,
    price,
    mileage,
    fuel_type,
    transmission,
    color,
    condition,
    description,
    images,
    video_url,
    three_sixty_url,
    in_stock,
    featured
FROM vehicle_inventory
WHERE id = ?;

-- =====================================================
-- Query: Get Vehicles by Make
-- =====================================================

SELECT 
    id,
    vin,
    make,
    model,
    trim,
    year,
    price,
    mileage,
    fuel_type,
    transmission,
    color,
    condition,
    description,
    images,
    in_stock,
    featured
FROM vehicle_inventory
WHERE make = ?
  AND in_stock = TRUE
ORDER BY year DESC, model;

-- =====================================================
-- Query: Get Featured Vehicles
-- =====================================================

SELECT 
    id,
    vin,
    make,
    model,
    trim,
    year,
    price,
    mileage,
    fuel_type,
    transmission,
    color,
    condition,
    description,
    images,
    in_stock,
    featured
FROM vehicle_inventory
WHERE featured = TRUE
  AND in_stock = TRUE
ORDER BY year DESC, make, model;

-- =====================================================
-- Query: Search Vehicles with Filters
-- =====================================================

-- Example: Search for vehicles with multiple filters
SELECT 
    id,
    vin,
    make,
    model,
    trim,
    year,
    price,
    mileage,
    fuel_type,
    transmission,
    color,
    condition,
    description,
    images,
    in_stock,
    featured
FROM vehicle_inventory
WHERE in_stock = TRUE
  AND (? IS NULL OR make = ?)
  AND (? IS NULL OR model = ?)
  AND (? IS NULL OR year >= ?)
  AND (? IS NULL OR year <= ?)
  AND (? IS NULL OR price >= ?)
  AND (? IS NULL OR price <= ?)
  AND (? IS NULL OR mileage <= ?)
  AND (? IS NULL OR fuel_type = ?)
  AND (? IS NULL OR transmission = ?)
  AND (? IS NULL OR condition = ?)
ORDER BY featured DESC, year DESC, price ASC;

-- =====================================================
-- Query: Update Vehicle Stock Status
-- =====================================================

-- Mark vehicle as sold/out of stock when purchased
UPDATE vehicle_inventory
SET in_stock = FALSE,
    last_updated = CURRENT_TIMESTAMP
WHERE id = ?;

-- =====================================================
-- Query: Update Vehicle Price
-- =====================================================

UPDATE vehicle_inventory
SET price = ?,
    last_updated = CURRENT_TIMESTAMP
WHERE id = ?;

-- =====================================================
-- Query: Get Available Makes (for filters)
-- =====================================================

SELECT DISTINCT make
FROM vehicle_inventory
WHERE in_stock = TRUE
ORDER BY make;

-- =====================================================
-- Query: Get Models by Make
-- =====================================================

SELECT DISTINCT model
FROM vehicle_inventory
WHERE make = ?
  AND in_stock = TRUE
ORDER BY model;

-- =====================================================
-- Query: Check if Inventory is Empty
-- =====================================================

-- Check total count of vehicles in inventory
SELECT COUNT(*) as total_vehicles FROM vehicle_inventory;

-- Check count of vehicles in stock
SELECT COUNT(*) as vehicles_in_stock FROM vehicle_inventory WHERE in_stock = TRUE;

-- Check count of new vehicles in stock
SELECT COUNT(*) as new_vehicles_in_stock 
FROM vehicle_inventory 
WHERE condition = 'New' AND in_stock = TRUE;

-- Check count of used vehicles in stock
SELECT COUNT(*) as used_vehicles_in_stock 
FROM vehicle_inventory 
WHERE condition = 'Used' AND in_stock = TRUE;

-- Quick check: Is inventory empty? (Returns 1 if empty, 0 if has vehicles)
SELECT 
    CASE 
        WHEN COUNT(*) = 0 THEN 'YES - Inventory is EMPTY'
        ELSE CONCAT('NO - Inventory has ', COUNT(*), ' vehicle(s)')
    END as inventory_status
FROM vehicle_inventory;

-- Detailed inventory summary
SELECT 
    COUNT(*) as total_vehicles,
    SUM(CASE WHEN in_stock = TRUE THEN 1 ELSE 0 END) as vehicles_in_stock,
    SUM(CASE WHEN condition = 'New' AND in_stock = TRUE THEN 1 ELSE 0 END) as new_vehicles,
    SUM(CASE WHEN condition = 'Used' AND in_stock = TRUE THEN 1 ELSE 0 END) as used_vehicles,
    SUM(CASE WHEN featured = TRUE AND in_stock = TRUE THEN 1 ELSE 0 END) as featured_vehicles,
    CASE 
        WHEN COUNT(*) = 0 THEN 'EMPTY'
        WHEN SUM(CASE WHEN in_stock = TRUE THEN 1 ELSE 0 END) = 0 THEN 'NO STOCK'
        ELSE 'HAS STOCK'
    END as inventory_status
FROM vehicle_inventory;

-- =====================================================
-- Notes:
-- =====================================================
-- 1. The 'images' field uses TEXT type to store JSON array of image paths
-- 2. Backend should map this to Vehicle.images: string[] format
-- 3. VIN (Vehicle Identification Number) should be unique
-- 4. Indexes are created for common query patterns (make, model, year, condition, etc.)
-- 5. When a vehicle is purchased, set in_stock = FALSE (or delete if preferred)
-- 6. Use parameterized queries to prevent SQL injection
-- 7. Run the inventory summary query above to check if your inventory is empty
-- =====================================================


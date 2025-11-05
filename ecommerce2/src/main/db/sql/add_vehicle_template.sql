-- =====================================================
-- Quick Vehicle Insert Template
-- Copy and modify these examples to add vehicles
-- =====================================================

USE ecom;

-- =====================================================
-- TEMPLATE 1: New Vehicle (Basic)
-- =====================================================
INSERT INTO vehicle_inventory (
    vin, make, model, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'YOUR_VIN_HERE',          -- Replace with unique VIN (17 characters)
    'Make',                   -- Replace: Ford, Toyota, Honda, etc.
    'Model',                  -- Replace: Everest, Fortuner, CR-V, etc.
    2024,                     -- Replace: Year
    1849000.00,               -- Replace: Price in PHP
    0,                        -- Replace: Mileage (0 for new)
    'Diesel',                 -- Replace: Diesel, Gasoline, Electric, Hybrid
    'Automatic',               -- Replace: Automatic, Manual, CVT
    'Color',                   -- Replace: Grey, White, Silver, Red, etc.
    'New',                     -- Use: 'New' or 'Used'
    'Description here',        -- Replace: Vehicle description
    '["/assets/img/image.jpg"]', -- Replace: Image path(s) as JSON array
    TRUE,                      -- TRUE = in stock, FALSE = out of stock
    FALSE                      -- TRUE = featured, FALSE = not featured
);

-- =====================================================
-- TEMPLATE 2: New Vehicle (With Trim)
-- =====================================================
INSERT INTO vehicle_inventory (
    vin, make, model, trim, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'YOUR_VIN_HERE',
    'Make',
    'Model',
    'Trim Level',              -- Optional: Trim level (e.g., 'Titanium', 'Limited')
    2024,
    1849000.00,
    0,
    'Diesel',
    'Automatic',
    'Color',
    'New',
    'Description here',
    '["/assets/img/image.jpg"]',
    TRUE,
    FALSE
);

-- =====================================================
-- TEMPLATE 3: Used Vehicle
-- =====================================================
INSERT INTO vehicle_inventory (
    vin, make, model, trim, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'YOUR_VIN_HERE',
    'Make',
    'Model',
    'Trim Level',              -- Optional
    2020,                      -- Replace: Year of vehicle
    800000.00,                 -- Replace: Used vehicle price
    50000,                     -- Replace: Actual mileage
    'Gasoline',
    'Automatic',
    'Color',
    'Used',                    -- Use 'Used' for pre-owned vehicles
    'Description here',
    '["/assets/img/image.jpg"]',
    TRUE,
    FALSE
);

-- =====================================================
-- TEMPLATE 4: Multiple Images
-- =====================================================
INSERT INTO vehicle_inventory (
    vin, make, model, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES (
    'YOUR_VIN_HERE',
    'Make',
    'Model',
    2024,
    1849000.00,
    0,
    'Diesel',
    'Automatic',
    'Color',
    'New',
    'Description here',
    '["/assets/img/image1.jpg", "/assets/img/image2.jpg", "/assets/img/image3.jpg"]',  -- Multiple images
    TRUE,
    TRUE                       -- Set to TRUE for featured vehicles
);

-- =====================================================
-- REAL EXAMPLES (Ready to Use)
-- =====================================================

-- Example 1: 2024 Ford Everest
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

-- Example 2: 2024 Nissan Terra (Featured)
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

-- Example 3: 2015 Toyota Corolla (Used)
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
    '["/assets/placeholder-car.jpg"]',
    TRUE,
    FALSE
);

-- =====================================================
-- QUICK BULK INSERT (Multiple Vehicles at Once)
-- =====================================================
INSERT INTO vehicle_inventory (
    vin, make, model, year, price, mileage, fuel_type, transmission, 
    color, condition, description, images, in_stock, featured
) VALUES 
    ('ABC123456789', 'Ford', 'Everest', 2024, 1849000.00, 0, 'Diesel', 'Automatic', 'Grey', 'New', 'Ford Everest SUV', '["/assets/img/2024 ford everest.avif"]', TRUE, FALSE),
    ('DEF456789012', 'Nissan', 'Terra', 2024, 1969000.00, 0, 'Diesel', 'Automatic', 'White', 'New', 'Nissan Terra SUV', '["/assets/img/2024 nissan terra.jpg"]', TRUE, TRUE),
    ('GHI789012345', 'Mitsubishi', 'Montero Sport', 2024, 1568000.00, 0, 'Diesel', 'Automatic', 'Silver', 'New', 'Mitsubishi Montero Sport', '["/assets/img/2024 mitsubishi montero sport.jpg"]', TRUE, TRUE),
    ('STU901234567', 'Toyota', 'Fortuner', 2024, 1749000.00, 0, 'Diesel', 'Automatic', 'White', 'New', 'Toyota Fortuner SUV', '["/assets/img/2024 toyota fortuner.jpg"]', TRUE, FALSE);

-- =====================================================
-- NOTES:
-- =====================================================
-- 1. VIN must be UNIQUE - cannot duplicate existing VINs
-- 2. Images must be JSON array format: '["path1", "path2"]'
-- 3. Price is in PHP (₱)
-- 4. Mileage: 0 for new vehicles, actual number for used
-- 5. Condition: Exactly 'New' or 'Used' (case-sensitive)
-- 6. After inserting, verify with: SELECT * FROM vehicle_inventory;
-- =====================================================


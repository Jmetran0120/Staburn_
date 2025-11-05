-- =====================================================
-- Quick Inventory Check Script
-- Run this to check if your vehicle inventory is empty
-- =====================================================

USE ecom;

-- Detailed inventory summary
SELECT 
    COUNT(*) as total_vehicles,
    SUM(CASE WHEN in_stock = TRUE THEN 1 ELSE 0 END) as vehicles_in_stock,
    SUM(CASE WHEN condition = 'New' AND in_stock = TRUE THEN 1 ELSE 0 END) as new_vehicles,
    SUM(CASE WHEN condition = 'Used' AND in_stock = TRUE THEN 1 ELSE 0 END) as used_vehicles,
    SUM(CASE WHEN featured = TRUE AND in_stock = TRUE THEN 1 ELSE 0 END) as featured_vehicles,
    CASE 
        WHEN COUNT(*) = 0 THEN 'EMPTY - No vehicles in database'
        WHEN SUM(CASE WHEN in_stock = TRUE THEN 1 ELSE 0 END) = 0 THEN 'NO STOCK - All vehicles are out of stock'
        ELSE 'HAS STOCK - Vehicles available'
    END as inventory_status
FROM vehicle_inventory;

-- Quick check: Is inventory empty?
SELECT 
    CASE 
        WHEN COUNT(*) = 0 THEN 'YES - Inventory is EMPTY'
        ELSE CONCAT('NO - Inventory has ', COUNT(*), ' vehicle(s) total, ', 
                    SUM(CASE WHEN in_stock = TRUE THEN 1 ELSE 0 END), ' in stock')
    END as inventory_status
FROM vehicle_inventory;


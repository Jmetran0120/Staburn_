-- =====================================================
-- Vehicle Images SQL Queries - Automotive Website
-- SQL queries for retrieving vehicle images in orders
-- =====================================================

-- =====================================================
-- Query: Get all vehicles in an order with images
-- =====================================================

-- Get order details with vehicle images for display
SELECT 
    o.id as order_id,
    o.customer_name,
    o.status as order_status,
    o.total_amount,
    o.created as purchase_date,
    oi.id as item_id,
    oi.product_id as vehicle_id,
    oi.product_name as vehicle_name,
    oi.product_description as vehicle_description,
    oi.product_image_file as vehicle_image_path,
    oi.price as vehicle_price,
    oi.status as vehicle_order_status
FROM order_data o
INNER JOIN order_item_data oi ON o.id = oi.order_id
WHERE o.id = 1  -- Replace with actual order_id
ORDER BY oi.id;

-- =====================================================
-- Query: Get customer's purchase history with vehicle images
-- =====================================================

-- View all vehicles purchased by a customer with images
SELECT 
    o.id as order_id,
    o.created as purchase_date,
    o.status as order_status,
    o.total_amount,
    oi.product_name as vehicle_name,
    oi.product_image_file as vehicle_image,
    oi.price as vehicle_price,
    oi.status as vehicle_status
FROM order_data o
INNER JOIN order_item_data oi ON o.id = oi.order_id
WHERE o.customer_id = 1  -- Replace with actual customer_id
ORDER BY o.created DESC, oi.id;

-- =====================================================
-- Query: Get all vehicles with images grouped by order
-- =====================================================

-- Group vehicles by order for display in customer dashboard
SELECT 
    o.id as order_id,
    o.customer_name,
    o.created as order_date,
    o.status,
    o.total_amount,
    GROUP_CONCAT(
        CONCAT(
            oi.product_name, 
            ' (', 
            oi.product_image_file, 
            ')'
        ) SEPARATOR ' | '
    ) as vehicles_with_images,
    COUNT(oi.id) as vehicle_count
FROM order_data o
LEFT JOIN order_item_data oi ON o.id = oi.order_id
WHERE o.customer_id = 1  -- Replace with actual customer_id
GROUP BY o.id, o.customer_name, o.created, o.status, o.total_amount
ORDER BY o.created DESC;

-- =====================================================
-- Query: Get order details with JSON-like image array
-- =====================================================

-- For applications that need image arrays
SELECT 
    o.id as order_id,
    o.customer_name,
    o.total_amount,
    JSON_ARRAYAGG(
        JSON_OBJECT(
            'vehicle_id', oi.product_id,
            'vehicle_name', oi.product_name,
            'image_path', oi.product_image_file,
            'price', oi.price,
            'status', oi.status
        )
    ) as vehicles_json
FROM order_data o
INNER JOIN order_item_data oi ON o.id = oi.order_id
WHERE o.id = 1  -- Replace with actual order_id
GROUP BY o.id, o.customer_name, o.total_amount;

-- =====================================================
-- Query: Check for orders with missing vehicle images
-- =====================================================

-- Find orders where vehicles don't have image paths set
SELECT 
    o.id as order_id,
    oi.id as item_id,
    oi.product_name as vehicle_name,
    oi.product_image_file as image_path,
    CASE 
        WHEN oi.product_image_file IS NULL OR oi.product_image_file = '' 
        THEN 'MISSING IMAGE' 
        ELSE 'HAS IMAGE' 
    END as image_status
FROM order_data o
INNER JOIN order_item_data oi ON o.id = oi.order_id
WHERE oi.product_image_file IS NULL 
   OR oi.product_image_file = ''
   OR oi.product_image_file NOT LIKE '/assets/images/vehicles/%'
ORDER BY o.created DESC;

-- =====================================================
-- Query: Get featured/popular vehicles with images from orders
-- =====================================================

-- Find most purchased vehicles with their images
SELECT 
    oi.product_id as vehicle_id,
    oi.product_name as vehicle_name,
    oi.product_image_file as vehicle_image,
    COUNT(*) as purchase_count,
    SUM(oi.price) as total_revenue,
    AVG(oi.price) as average_price
FROM order_item_data oi
WHERE oi.status IN ('Paid', 'Completed', 'Ordered')
  AND oi.product_image_file IS NOT NULL
  AND oi.product_image_file != ''
GROUP BY oi.product_id, oi.product_name, oi.product_image_file
ORDER BY purchase_count DESC
LIMIT 10;

-- =====================================================
-- Query: Update vehicle image path for an order item
-- =====================================================

-- Update image path if vehicle image location changes
UPDATE order_item_data 
SET product_image_file = '/assets/images/vehicles/new-path/image.jpg',
    last_updated = CURRENT_TIMESTAMP
WHERE id = 1;  -- Replace with actual order_item_data id

-- =====================================================
-- Query: Bulk update image paths for a specific vehicle
-- =====================================================

-- Update all order items for a specific vehicle model
UPDATE order_item_data 
SET product_image_file = '/assets/images/vehicles/honda-civic-2023-main.jpg',
    last_updated = CURRENT_TIMESTAMP
WHERE product_id = 5  -- Replace with actual vehicle/product_id
  AND product_name LIKE '%Honda Civic%';


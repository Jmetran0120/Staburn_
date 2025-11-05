-- =====================================================
-- Purchase Flow SQL Script - Automotive Website
-- Complete SQL flow for when a customer purchases vehicles
-- =====================================================

-- =====================================================
-- STEP 1: Create Order Record
-- When customer clicks "Purchase" or "Checkout"
-- =====================================================

-- Insert the main order record
INSERT INTO order_data (
    customer_id, 
    customer_name, 
    status, 
    total_amount, 
    shipping_address, 
    payment_method
) 
VALUES (
    1,                              -- customer_id (from logged in user)
    'John Smith',                   -- customer_name (from customer data)
    'PENDING',                      -- Initial status
    0.00,                           -- Will be updated after calculating items
    '456 Auto Drive, Detroit, MI 48201',  -- shipping_address (delivery address)
    'Auto Financing'               -- payment_method (Financing, Cash, Trade-In, etc.)
);

-- Get the order ID that was just created (last_insert_id in MySQL)
SET @order_id = LAST_INSERT_ID();

-- =====================================================
-- STEP 2: Insert Order Items (Vehicles Purchased)
-- For each vehicle in the cart, create an order_item_data record
-- =====================================================

-- Example: Customer purchases Vehicle ID 5 (2023 Honda Civic)
INSERT INTO order_item_data (
    order_id,
    customer_id,
    customer_name,
    product_id,
    product_name,
    product_description,
    product_category_name,
    product_image_file,
    product_unit_of_measure,
    quantity,
    price,
    status
)
VALUES (
    @order_id,                      -- order_id (from step 1)
    1,                              -- customer_id
    'John Smith',                   -- customer_name
    5,                              -- product_id (vehicle being purchased)
    '2023 Honda Civic',             -- product_name (Year Make Model)
    '2023 Honda Civic EX-L with navigation. Low mileage, excellent condition. One owner, all service records included.',  -- product_description
    'Sedans',                       -- product_category_name (Sedans, SUVs, Trucks, etc.)
    '/assets/images/vehicles/honda-civic-2023-main.jpg',  -- product_image_file (main vehicle image)
    'unit',                         -- product_unit_of_measure (always 'unit' for vehicles)
    1.0,                            -- quantity (usually 1 for vehicles)
    24500.00,                       -- price (vehicle price)
    'Created'                       -- status (Created, Ordered, Paid, etc.)
);

-- Example: Customer purchases another Vehicle ID 12 (2022 Toyota Camry)
INSERT INTO order_item_data (
    order_id,
    customer_id,
    customer_name,
    product_id,
    product_name,
    product_description,
    product_category_name,
    product_image_file,
    product_unit_of_measure,
    quantity,
    price,
    status
)
VALUES (
    @order_id,                      -- Same order_id
    1,                              -- customer_id
    'John Smith',                   -- customer_name
    12,                             -- product_id
    '2022 Toyota Camry XLE',       -- product_name
    '2022 Toyota Camry XLE with premium features. Certified Pre-Owned, 15,000 miles. Includes extended warranty.',  -- product_description
    'Sedans',                       -- product_category_name
    '/assets/images/vehicles/toyota-camry-2022-main.jpg',  -- product_image_file
    'unit',                         -- product_unit_of_measure
    1.0,                            -- quantity
    28500.00,                       -- price
    'Created'                       -- status
);

-- =====================================================
-- STEP 3: Calculate and Update Order Total
-- Sum up all order items and update the order total_amount
-- =====================================================

-- Calculate total from all order items and update order
UPDATE order_data 
SET total_amount = (
    SELECT SUM(quantity * price) 
    FROM order_item_data 
    WHERE order_id = @order_id
)
WHERE id = @order_id;

-- =====================================================
-- STEP 4: Update Order Status (Optional)
-- After all items are added, update status
-- =====================================================

-- Change order status from PENDING to ORDERED after purchase is complete
UPDATE order_data 
SET status = 'ORDERED',
    last_updated = CURRENT_TIMESTAMP
WHERE id = @order_id;

-- =====================================================
-- QUERY: View Complete Purchase Details
-- =====================================================

-- Get order with all items for a purchase (including vehicle images)
SELECT 
    o.id as order_id,
    o.customer_name,
    o.status as order_status,
    o.total_amount,
    o.created as order_date,
    oi.product_name as vehicle_name,
    oi.product_description as vehicle_description,
    oi.product_category_name as vehicle_category,
    oi.product_image_file as vehicle_image,
    oi.quantity,
    oi.price as vehicle_price,
    (oi.quantity * oi.price) as item_total,
    oi.status as item_status
FROM order_data o
LEFT JOIN order_item_data oi ON o.id = oi.order_id
WHERE o.id = @order_id
ORDER BY oi.id;

-- =====================================================
-- QUERY: View Customer Purchase History
-- =====================================================

-- Get all purchases for a customer
SELECT 
    o.id as order_id,
    o.status,
    o.total_amount,
    o.created as purchase_date,
    COUNT(oi.id) as item_count
FROM order_data o
LEFT JOIN order_item_data oi ON o.id = oi.order_id
WHERE o.customer_id = 1
GROUP BY o.id, o.status, o.total_amount, o.created
ORDER BY o.created DESC;


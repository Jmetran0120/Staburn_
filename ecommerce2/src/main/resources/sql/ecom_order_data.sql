-- =====================================================
-- Order Data SQL Script - Automotive Website
-- This script contains SQL statements for storing orders
-- when customers purchase vehicles
-- =====================================================

-- Create order_data table if it doesn't exist (for reference)
-- Note: Hibernate/JPA typically creates this automatically
-- This is just for documentation purposes

CREATE TABLE IF NOT EXISTS order_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    customer_name VARCHAR(255),
    status VARCHAR(50) DEFAULT 'PENDING',
    total_amount DECIMAL(10,2) DEFAULT 0.00,
    shipping_address VARCHAR(500),
    payment_method VARCHAR(100),
    notes TEXT,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =====================================================
-- Example: Insert a new order when customer purchases
-- =====================================================

-- When a customer purchases vehicles, insert an order record
INSERT INTO order_data (customer_id, customer_name, status, total_amount, shipping_address, payment_method, notes)
VALUES (
    1,                              -- customer_id
    'John Smith',                   -- customer_name
    'PENDING',                      -- status (PENDING, INVOICED, PAID, SHIPPED, COMPLETED, CANCELLED, SUSPENDED)
    53000.00,                       -- total_amount (sum of all vehicles purchased)
    '456 Auto Drive, Detroit, MI 48201',  -- shipping_address (delivery/pickup address)
    'Auto Financing',              -- payment_method (Auto Financing, Cash, Trade-In, Lease)
    'Customer requested test drive before delivery'  -- notes
);

-- =====================================================
-- Example: Update order status after payment
-- =====================================================

-- After payment is processed, update order status
UPDATE order_data 
SET status = 'PAID', 
    last_updated = CURRENT_TIMESTAMP
WHERE id = 1;

-- =====================================================
-- Example: Query orders for a specific customer
-- =====================================================

-- Get all orders for customer ID 1
SELECT * FROM order_data 
WHERE customer_id = 1 
ORDER BY created DESC;

-- =====================================================
-- Example: Get total amount spent by customer
-- =====================================================

-- Calculate total amount spent by a customer on vehicle purchases
SELECT customer_id, customer_name, SUM(total_amount) as total_spent, COUNT(*) as total_vehicles_purchased
FROM order_data
WHERE customer_id = 1
GROUP BY customer_id, customer_name;

-- =====================================================
-- Example: Get order count by status
-- =====================================================

-- Count orders by status
SELECT status, COUNT(*) as order_count
FROM order_data
GROUP BY status;


-- =====================================================
-- User Data SQL Script - Authentication System
-- This script creates the user_data table for user authentication
-- =====================================================

-- =====================================================
-- Create User Data Table
-- =====================================================

CREATE TABLE IF NOT EXISTS user_data (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL COMMENT 'User email address (unique)',
    password VARCHAR(255) NOT NULL COMMENT 'Hashed password (BCrypt)',
    name VARCHAR(255) NOT NULL COMMENT 'User full name',
    role VARCHAR(20) NOT NULL DEFAULT 'customer' COMMENT 'User role: customer or admin',
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Account creation timestamp',
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last update timestamp',
    
    -- Indexes for common queries
    INDEX idx_email (email),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User authentication and authorization data';

-- =====================================================
-- Example: Insert Admin User
-- =====================================================

-- Note: Password should be hashed using BCrypt. 
-- Example password "admin123" hashed: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
-- In production, always hash passwords before inserting!

-- INSERT INTO user_data (email, password, name, role)
-- VALUES (
--     'admin@autodeal.com',
--     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- password: admin123
--     'Administrator',
--     'admin'
-- );

-- =====================================================
-- Example: Insert Customer User
-- =====================================================

-- INSERT INTO user_data (email, password, name, role)
-- VALUES (
--     'customer@example.com',
--     '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- password: admin123
--     'John Customer',
--     'customer'
-- );

-- =====================================================
-- Query: Get all users
-- =====================================================

-- SELECT id, email, name, role, created, last_updated 
-- FROM user_data 
-- ORDER BY created DESC;

-- =====================================================
-- Query: Find user by email
-- =====================================================

-- SELECT id, email, name, role 
-- FROM user_data 
-- WHERE email = 'user@example.com';

-- =====================================================
-- Query: Get all admin users
-- =====================================================

-- SELECT id, email, name, created 
-- FROM user_data 
-- WHERE role = 'admin';

-- =====================================================
-- Query: Update user role
-- =====================================================

-- UPDATE user_data 
-- SET role = 'admin', 
--     last_updated = CURRENT_TIMESTAMP
-- WHERE email = 'user@example.com';

-- =====================================================
-- Query: Delete user
-- =====================================================

-- DELETE FROM user_data 
-- WHERE email = 'user@example.com';


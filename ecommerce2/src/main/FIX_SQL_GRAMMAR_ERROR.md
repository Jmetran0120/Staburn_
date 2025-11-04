# Fix SQLGrammarException Error

## Problem
```
could not extract ResultSet; SQL [n/a]; 
nested exception is org.hibernate.exception.SQLGrammarException
```

## Common Causes & Solutions

### 1. Table Doesn't Exist (Most Common)

**Solution:** Create the table manually or let Hibernate create it.

**Option A: Let Hibernate auto-create (Recommended)**
- Your `application.yml` has `ddl-auto: update`
- Restart your Spring Boot application
- Hibernate will create the table automatically
- Check backend logs for table creation messages

**Option B: Create table manually**
```bash
mysql -u root -p ecom < ../src/sql/ecom_vehicle_inventory.sql
```

Or in MySQL:
```sql
USE ecom;
-- Run CREATE TABLE statement from sql/ecom_vehicle_inventory.sql
```

### 2. Verify Table Exists

Check if table exists:
```sql
USE ecom;
SHOW TABLES LIKE 'vehicle_inventory';
```

If it doesn't exist, create it:
```sql
CREATE TABLE IF NOT EXISTS vehicle_inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vin VARCHAR(17) UNIQUE NOT NULL,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(100) NOT NULL,
    trim VARCHAR(100) DEFAULT NULL,
    year INT NOT NULL,
    price DECIMAL(12, 2) NOT NULL,
    mileage INT DEFAULT 0 NOT NULL,
    fuel_type VARCHAR(50) NOT NULL,
    transmission VARCHAR(50) NOT NULL,
    color VARCHAR(50) NOT NULL,
    `condition` VARCHAR(20) NOT NULL DEFAULT 'New',
    description TEXT,
    images TEXT,
    video_url VARCHAR(500) DEFAULT NULL,
    three_sixty_url VARCHAR(500) DEFAULT NULL,
    in_stock BOOLEAN DEFAULT TRUE NOT NULL,
    featured BOOLEAN DEFAULT FALSE NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_make (make),
    INDEX idx_model (model),
    INDEX idx_year (year),
    INDEX idx_condition (`condition`),
    INDEX idx_price (price),
    INDEX idx_featured (featured),
    INDEX idx_in_stock (in_stock),
    INDEX idx_make_model (make, model),
    INDEX idx_condition_featured (`condition`, featured)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

### 3. Check Column Names Match

Verify table structure:
```sql
DESCRIBE vehicle_inventory;
```

Or:
```sql
SHOW COLUMNS FROM vehicle_inventory;
```

### 4. Reserved Word Issue

The word `condition` is a reserved word in MySQL. It's already escaped in the entity with backticks.

### 5. Database Connection Issue

Verify database connection:
```sql
-- Test connection
SELECT DATABASE();
-- Should return: ecom
```

### 6. Check Hibernate Logs

Enable detailed logging in `application.yml`:
```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

This will show you the exact SQL query that's failing.

## Quick Fix Steps

1. **Stop your Spring Boot application**

2. **Check if table exists:**
   ```bash
   mysql -u root -p ecom -e "SHOW TABLES LIKE 'vehicle_inventory';"
   ```

3. **If table doesn't exist, create it:**
   ```bash
   mysql -u root -p ecom < ../src/sql/ecom_vehicle_inventory.sql
   ```

4. **If table exists, verify structure:**
   ```bash
   mysql -u root -p ecom -e "DESCRIBE vehicle_inventory;"
   ```

5. **Restart Spring Boot application**

6. **Check backend logs** for any errors

## Alternative: Use Hibernate Auto-Create

If you want Hibernate to create the table automatically:

1. Make sure `ddl-auto: update` is set in `application.yml` (it is)
2. Restart application
3. Hibernate will create the table on first startup
4. Check logs for: `Hibernate: create table vehicle_inventory...`

## Verify Fix

After fixing, test the endpoint:
```bash
curl http://localhost:8080/api/vehicle/status
```

Or check inventory:
```bash
curl http://localhost:8080/api/vehicle
```

## Still Having Issues?

1. Check the full stack trace in your backend logs
2. Look for the exact SQL query that's failing
3. Verify the database name is correct (`ecom`)
4. Check MySQL user permissions
5. Verify MySQL is running


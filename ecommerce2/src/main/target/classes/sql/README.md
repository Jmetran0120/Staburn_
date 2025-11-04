# SQL Scripts - Automotive Website

This folder contains SQL scripts for the automotive website.

## File Organization

### 📄 `ecom_order_data.sql`
Contains SQL statements for the `order_data` table:
- Table structure reference
- Insert new orders when customers purchase vehicles
- Update order status (PENDING → PAID → SHIPPED → COMPLETED)
- Query examples for customer orders and vehicle purchase totals

### 📄 `ecom_purchase_flow.sql`
Complete SQL workflow for the vehicle purchase process:
1. **Create Order** - Insert order record when customer purchases vehicles
2. **Insert Order Items** - Store each vehicle purchased as order_item_data with images
3. **Calculate Total** - Update order total_amount from all vehicles
4. **Update Status** - Change order status through purchase lifecycle
5. **Query Examples** - View purchase details with vehicle images and customer history

### 📄 `ecom_vehicle_images.sql`
SQL queries specifically for vehicle images in orders:
- Get order details with vehicle images
- Customer purchase history with images
- Check for missing vehicle images
- Update image paths
- Popular vehicles with images from order data

### 📄 `ecom_vehicle_inventory.sql`
**NEW: Vehicle Inventory Management**
Complete SQL schema and queries for the vehicle inventory catalog:
- Create `vehicle_inventory` table to store all vehicles (new and used)
- Insert new and used vehicles
- Query vehicles by condition (new/used), make, model, filters
- Get featured vehicles
- Search with multiple filters (price, year, mileage, fuel type, etc.)
- Update stock status when vehicles are sold
- Get available makes/models for filter dropdowns

## How It Works

When a customer purchases vehicles:

1. **Frontend** calls `POST /api/order` (or `PUT /api/order`) with order data
2. **Backend** (`OrderController`) receives the request
3. **SQL** inserts into `order_data` table (handled by JPA/Hibernate)
4. **Frontend** calls `PUT /api/orderitem` for each vehicle purchased
5. **Backend** (`OrderItemController`) receives order items with vehicle details and images
6. **SQL** inserts into `order_item_data` table for each vehicle

The SQL scripts in this folder show the raw SQL equivalent of what happens automatically through the Java backend.

## Database Tables

### `vehicle_inventory` ⭐ **NEW - Recommended**
Stores all available vehicles in your inventory catalog:
- `id` - Vehicle ID (primary key)
- `vin` - Vehicle Identification Number (unique)
- `make` - Manufacturer (Ford, Toyota, Honda, etc.)
- `model` - Model name
- `trim` - Trim level (optional)
- `year` - Model year
- `price` - Vehicle price in PHP
- `mileage` - Current mileage (0 for new vehicles)
- `fuel_type` - Fuel type (Diesel, Gasoline, Electric, Hybrid)
- `transmission` - Transmission type (Automatic, Manual, CVT)
- `color` - Vehicle color
- `condition` - "New" or "Used"
- `description` - Detailed description
- `images` - JSON array of image paths
- `video_url` - Optional video URL
- `three_sixty_url` - Optional 360-degree view URL
- `in_stock` - Whether vehicle is currently available
- `featured` - Whether vehicle is featured/promoted
- `created` - When vehicle was added
- `last_updated` - Last update timestamp

**Why this table is important:**
- Centralizes all vehicle data in one place
- Enables efficient searching and filtering
- Tracks stock availability
- Supports all your API endpoints (`/api/vehicle`, `/api/vehicle/new`, `/api/vehicle/used`, etc.)
- Replaces hardcoded sample data with real database data

### `order_data`
Stores the main order information for vehicle purchases:
- `id` - Order ID (auto-generated)
- `customer_id` - Customer who made the purchase
- `customer_name` - Customer name
- `status` - Order status (PENDING, INVOICED, PAID, SHIPPED, COMPLETED, CANCELLED, SUSPENDED)
- `total_amount` - Total cost of the vehicle(s) purchased
- `shipping_address` - Delivery/pickup address
- `payment_method` - Payment method used (Auto Financing, Cash, Trade-In, Lease)
- `notes` - Additional notes (test drive requests, special delivery instructions, etc.)
- `created` - Order creation timestamp
- `last_updated` - Last update timestamp

### `order_item_data`
Stores individual vehicles in an order:
- `id` - Order item ID (auto-generated)
- `order_id` - Links to order_data.id
- `customer_id` - Customer ID
- `product_id` - Vehicle ID purchased
- `product_name` - Vehicle name (Year Make Model, e.g., "2023 Honda Civic")
- `product_description` - Vehicle description
- `product_category_name` - Vehicle category (Sedans, SUVs, Trucks, etc.)
- `product_image_file` - **Vehicle image path** (e.g., `/assets/images/vehicles/honda-civic-2023-main.jpg`)
- `product_unit_of_measure` - Always "unit" for vehicles
- `quantity` - Usually 1 for vehicles
- `price` - Vehicle price
- `status` - Item status (Created, Ordered, Paid, etc.)
- `created` - Item creation timestamp
- `last_updated` - Last update timestamp

## Vehicle Images

Vehicle images are stored in the `product_image_file` field of `order_item_data`:
- Image paths reference assets folder: `/assets/images/vehicles/[vehicle-name]-[year]-main.jpg`
- Multiple images per vehicle can be stored as JSON array or comma-separated string
- Main image is typically used in order confirmations and purchase history

Example image paths:
- `/assets/images/vehicles/honda-civic-2023-main.jpg`
- `/assets/images/vehicles/toyota-camry-2022-main.jpg`
- `/assets/images/vehicles/ford-f150-2023-main.jpg`

## Data Flow

### Inventory Management (NEW)
1. **Add Vehicles** - Insert vehicles into `vehicle_inventory` table
2. **Frontend** calls `/api/vehicle/*` endpoints to browse inventory
3. **Backend** queries `vehicle_inventory` table and returns results
4. **When Purchased** - Set `in_stock = FALSE` (or delete) from `vehicle_inventory`
5. **Order Created** - Insert purchase record into `order_data` and `order_item_data`

### Purchase Flow (Existing)
1. **Frontend** calls `POST /api/order` (or `PUT /api/order`) with order data
2. **Backend** (`OrderController`) receives the request
3. **SQL** inserts into `order_data` table (handled by JPA/Hibernate)
4. **Frontend** calls `PUT /api/orderitem` for each vehicle purchased
5. **Backend** (`OrderItemController`) receives order items with vehicle details and images
6. **SQL** inserts into `order_item_data` table for each vehicle

The SQL scripts in this folder show the raw SQL equivalent of what happens automatically through the Java backend.

## Related Files

- **Frontend**: `/src/app/models/vehicle.model.ts` - Vehicle TypeScript interface
- **Frontend**: `/src/app/service/vehicle.service.ts` - Vehicle API service
- **Frontend**: `/src/app/components/vehicle-catalog/vehicle-catalog.component.ts` - Catalog component
- Backend: `/ecommerce 2/src/main/java/com/gabriel/controller/OrderController.java`
- Backend: `/ecommerce 2/src/main/java/com/gabriel/controller/OrderItemController.java`
- Backend: `/ecommerce 2/src/main/java/com/gabriel/entity/OrderData.java`
- Backend: `/ecommerce 2/src/main/java/com/gabriel/repository/OrderDataRepository.java`

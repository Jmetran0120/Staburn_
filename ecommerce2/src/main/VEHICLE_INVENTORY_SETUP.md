# Vehicle Inventory Backend Setup

This document describes the complete vehicle inventory backend implementation.

## Files Created

### 1. Entity
- **`src/main/java/com/gabriel/entity/VehicleInventory.java`**
  - JPA entity mapping to `vehicle_inventory` table
  - Matches the TypeScript `Vehicle` model from the frontend
  - Includes indexes for performance optimization
  - Handles JSON storage for images array

### 2. Repository
- **`src/main/java/com/gabriel/repository/VehicleInventoryRepository.java`**
  - Extends `CrudRepository` for basic CRUD operations
  - Custom query methods for filtering by condition, make, model, etc.
  - Search method with multiple filter parameters
  - Methods for getting distinct makes/models for filter dropdowns

### 3. Controller
- **`src/main/java/com/gabriel/controller/VehicleController.java`**
  - Implements all API endpoints as specified in `API_ENDPOINTS.md`
  - Handles JSON serialization/deserialization for images array
  - Converts between database format and frontend format
  - Includes admin endpoints (create, update, delete)

## API Endpoints Implemented

### Public Endpoints

1. **GET** `/api/vehicle` - Get all vehicles in stock
2. **GET** `/api/vehicle/{id}` - Get vehicle by ID
3. **GET** `/api/vehicle/new` - Get all new vehicles
4. **GET** `/api/vehicle/used` - Get all used vehicles
5. **GET** `/api/vehicle/make/{make}` - Get vehicles by make
6. **GET** `/api/vehicle/featured` - Get featured vehicles
7. **POST** `/api/vehicle/search` - Search vehicles with filters
8. **GET** `/api/vehicle/makes` - Get distinct makes (for filter dropdown)
9. **GET** `/api/vehicle/models/{make}` - Get models by make (for filter dropdown)

### Admin Endpoints

10. **POST** `/api/vehicle` - Create new vehicle
11. **PUT** `/api/vehicle/{id}` - Update vehicle
12. **DELETE** `/api/vehicle/{id}` - Delete vehicle

## Database Setup

1. **Create the table** - Run the SQL script:
   ```sql
   -- See: src/sql/ecom_vehicle_inventory.sql
   ```

2. **Insert sample data** - The SQL file includes example INSERT statements for new and used vehicles.

## Features

### JSON Images Handling
- Images are stored as JSON string in the database (`images` column)
- Automatically converted to/from `List<String>` when communicating with frontend
- Uses Jackson `ObjectMapper` for JSON parsing

### Data Mapping
- Database column names (snake_case) are mapped to Java fields (camelCase)
- Response format matches the TypeScript `Vehicle` interface exactly
- Uses `@JsonProperty` annotations for field name mapping

### CORS Configuration
- All endpoints are configured with `@CrossOrigin(origins = "*")` for development
- In production, restrict to your Angular app domain

## Next Steps

1. **Start the backend server** - The Spring Boot application should automatically detect the new entity
2. **Run the SQL script** - Create the table and insert sample data
3. **Test the endpoints** - Use Postman or the Angular frontend to test
4. **Remove sample data fallback** - Once backend is working, you can remove `getSampleVehicles()` from the Angular component

## Example Request/Response

### Create Vehicle (POST /api/vehicle)
```json
{
  "vin": "ABC123456789",
  "make": "Ford",
  "model": "Everest",
  "trim": "Titanium",
  "year": 2024,
  "price": 1849000.00,
  "mileage": 0,
  "fuelType": "Diesel",
  "transmission": "Automatic",
  "color": "Grey",
  "condition": "New",
  "description": "Ford Everest SUV - A powerful and rugged SUV perfect for adventure.",
  "images": ["/assets/img/2024 ford everest.avif"],
  "inStock": true,
  "featured": false
}
```

### Search Vehicles (POST /api/vehicle/search)
```json
{
  "make": "Toyota",
  "yearMin": 2020,
  "priceMax": 2000000,
  "condition": "New"
}
```

## Troubleshooting

### ObjectMapper not found
- Spring Boot automatically provides `ObjectMapper` bean
- Make sure `jackson-databind` is in your `pom.xml` (already included)

### Table not created
- Check Hibernate `ddl-auto` setting in `application.yml`
- Set to `update` or `create` to auto-create tables
- Or manually run the SQL script

### Images not parsing correctly
- Ensure images are sent as JSON array: `["path1", "path2"]`
- Database stores as JSON string: `'["path1","path2"]'`
- Backend automatically converts between formats

## Integration with Frontend

The Angular `VehicleService` in `src/app/service/vehicle.service.ts` is already configured to use these endpoints. Once the backend is running, the frontend will automatically connect to it instead of using sample data.


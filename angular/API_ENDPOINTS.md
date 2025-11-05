# Backend API Endpoints for AutoDeal

This document outlines the required backend API endpoints that need to be implemented to match the frontend.

## Base URL
- Development: `http://localhost:8080`
- Production: Configure in `environments/environment.ts`

## Vehicle Endpoints

### 1. Get All Vehicles
**GET** `/api/vehicle`
- Returns: Array of all vehicles
- Response: `Vehicle[]`

### 2. Get Vehicle by ID
**GET** `/api/vehicle/{id}`
- Parameters: `id` (number)
- Returns: Single vehicle object
- Response: `Vehicle`

### 3. Get New Vehicles
**GET** `/api/vehicle/new`
- Returns: Array of vehicles where `condition = 'New'`
- Response: `Vehicle[]`

### 4. Get Used Vehicles
**GET** `/api/vehicle/used`
- Returns: Array of vehicles where `condition = 'Used'`
- Response: `Vehicle[]`

### 5. Get Vehicles by Make
**GET** `/api/vehicle/make/{make}`
- Parameters: `make` (string) - e.g., "Ford", "Toyota"
- Returns: Array of vehicles filtered by make
- Response: `Vehicle[]`

### 6. Get Featured Vehicles
**GET** `/api/vehicle/featured`
- Returns: Array of vehicles where `featured = true`
- Response: `Vehicle[]`

### 7. Search Vehicles (with filters)
**POST** `/api/vehicle/search`
- Request Body: `VehicleFilter` object
- Returns: Array of vehicles matching the filters
- Response: `Vehicle[]`

## Vehicle Data Model

The backend should return vehicles in the following JSON format:

```typescript
{
  id: number;
  vin: string;
  make: string;
  model: string;
  trim?: string;
  year: number;
  price: number;
  mileage: number;
  fuelType: string;
  transmission: string;
  color: string;
  condition: string;  // "New" or "Used"
  description: string;
  images: string[];  // Array of image URLs
  videoUrl?: string;
  threeSixtyUrl?: string;
  inStock: boolean;
  featured: boolean;
}
```

### Example Vehicle JSON:
```json
{
  "id": 1,
  "vin": "ABC123",
  "make": "Ford",
  "model": "Everest",
  "trim": null,
  "year": 2024,
  "price": 1849000,
  "mileage": 0,
  "fuelType": "Diesel",
  "transmission": "Automatic",
  "color": "Grey",
  "condition": "New",
  "description": "Ford Everest SUV - A powerful and rugged SUV...",
  "images": ["/assets/img/2024 ford everest.avif"],
  "videoUrl": null,
  "threeSixtyUrl": null,
  "inStock": true,
  "featured": false
}
```

## VehicleFilter Model (for search endpoint)

```typescript
{
  make?: string;
  model?: string;
  yearMin?: number;
  yearMax?: number;
  priceMin?: number;
  priceMax?: number;
  mileageMax?: number;
  fuelType?: string;
  transmission?: string;
  condition?: string;
}
```

## Error Handling

All endpoints should return appropriate HTTP status codes:
- `200 OK` - Success
- `404 Not Found` - Vehicle not found
- `400 Bad Request` - Invalid request parameters
- `500 Internal Server Error` - Server error

On error, the frontend will fall back to sample data for demonstration purposes.

## CORS Configuration

The backend must allow CORS requests from the Angular frontend:
- Allow Origin: `http://localhost:4200` (development)
- Allow Methods: `GET`, `POST`, `PUT`, `DELETE`
- Allow Headers: `Content-Type`, `Authorization`

## Notes

1. Image paths should be relative to the Angular assets folder (e.g., `/assets/img/...`)
2. The frontend has fallback sample data that will be used if the API is unavailable
3. All vehicle prices are in PHP (₱) format
4. The frontend expects arrays even for single-item responses (except GET by ID)


export interface Vehicle {
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
  condition: string;
  description: string;
  images: string[];
  videoUrl?: string;
  threeSixtyUrl?: string;
  inStock: boolean;
  featured: boolean;
}

export interface VehicleFilter {
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



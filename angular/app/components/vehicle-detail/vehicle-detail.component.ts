import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Vehicle } from '../../models/vehicle.model';
import { VehicleService } from '../../service/vehicle.service';
import { CartService } from '../../service/cart.service';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-vehicle-detail',
  templateUrl: './vehicle-detail.component.html',
  styleUrls: ['./vehicle-detail.component.css']
})
export class VehicleDetailComponent implements OnInit {
  vehicle: Vehicle | null = null;
  selectedImageIndex = 0;
  show360View = false;
  variantCount: number = 0;

  constructor(
    private route: ActivatedRoute,
    private vehicleService: VehicleService,
    private cartService: CartService
  ) { }

  ngOnInit(): void {
    const vehicleId = this.route.snapshot.paramMap.get('id');
    if (vehicleId) {
      this.loadVehicle(parseInt(vehicleId));
    }
  }

  loadVehicle(id: number): void {
    this.vehicleService.getVehicleById(id).pipe(
      catchError(error => {
        console.error('Error loading vehicle:', error);
        const vehicles = this.getSampleVehicles();
        this.vehicle = vehicles.find(v => v.id === id) || null;
        if (this.vehicle) {
          this.variantCount = this.getVariantCount(this.vehicle);
        }
        return of(null);
      })
    ).subscribe((vehicle: Vehicle | null) => {
      if (vehicle) {
        this.vehicle = vehicle;
        this.variantCount = this.getVariantCount(vehicle);
      } else if (!this.vehicle) {
        const vehicles = this.getSampleVehicles();
        this.vehicle = vehicles.find(v => v.id === id) || null;
        if (this.vehicle) {
          this.variantCount = this.getVariantCount(this.vehicle);
        }
      }
    });
  }

  getSampleVehicles(): Vehicle[] {
    return [
      {
        id: 1,
        vin: 'ABC123',
        make: 'Ford',
        model: 'Everest',
        year: 2024,
        price: 1849000,
        mileage: 0,
        fuelType: 'Diesel',
        transmission: 'Automatic',
        color: 'Grey',
        condition: 'New',
        description: 'Ford Everest SUV - A powerful and rugged SUV perfect for both city driving and off-road adventures. Features advanced safety systems, modern technology, and exceptional fuel efficiency.',
        images: ['/assets/img/2024 ford everest.avif'],
        inStock: true,
        featured: false
      },
      {
        id: 2,
        vin: 'DEF456',
        make: 'Nissan',
        model: 'Terra',
        year: 2024,
        price: 1969000,
        mileage: 0,
        fuelType: 'Diesel',
        transmission: 'Automatic',
        color: 'White',
        condition: 'New',
        description: 'Nissan Terra SUV - Experience premium comfort and robust performance. Equipped with cutting-edge features, spacious interior, and reliable diesel engine.',
        images: ['/assets/img/2024 nissan terra.jpg'],
        inStock: true,
        featured: true
      },
      {
        id: 3,
        vin: 'GHI789',
        make: 'Mitsubishi',
        model: 'Montero Sport',
        year: 2024,
        price: 1568000,
        mileage: 0,
        fuelType: 'Diesel',
        transmission: 'Automatic',
        color: 'Silver',
        condition: 'New',
        description: 'Mitsubishi Montero Sport - A versatile SUV combining style, performance, and value. Perfect for families seeking adventure with confidence.',
        images: ['/assets/img/2024 mitsubishi montero sport.jpg'],
        inStock: true,
        featured: true
      },
      {
        id: 7,
        vin: 'STU901',
        make: 'Toyota',
        model: 'Fortuner',
        year: 2024,
        price: 1749000,
        mileage: 0,
        fuelType: 'Diesel',
        transmission: 'Automatic',
        color: 'White',
        condition: 'New',
        description: 'Toyota Fortuner SUV - Built tough with legendary Toyota reliability. Spacious, powerful, and packed with advanced safety and convenience features.',
        images: ['/assets/img/2024 toyota fortuner.jpg'],
        inStock: true,
        featured: false
      },
      {
        id: 8,
        vin: 'VWX234',
        make: 'Honda',
        model: 'CR-V',
        year: 2024,
        price: 1899000,
        mileage: 0,
        fuelType: 'Gasoline',
        transmission: 'CVT',
        color: 'Silver',
        condition: 'New',
        description: 'Honda CR-V Crossover - The perfect blend of efficiency, comfort, and performance. Features Honda Sensing safety suite and innovative design.',
        images: ['/assets/img/2024 honda cr-v.jpg'],
        inStock: true,
        featured: true
      },
      {
        id: 9,
        vin: 'YZA567',
        make: 'Mazda',
        model: 'CX-5',
        year: 2024,
        price: 1799000,
        mileage: 0,
        fuelType: 'Gasoline',
        transmission: 'Automatic',
        color: 'Red',
        condition: 'New',
        description: 'Mazda CX-5 Crossover - Sophisticated design meets exceptional performance. Experience the thrill of driving with Mazda\'s SKYACTIV technology.',
        images: ['/assets/img/2024 mazda cx-5.jpg'],
        inStock: true,
        featured: false
      }
    ];
  }

  getVariantCount(vehicle: Vehicle): number {
    const counts: { [key: string]: number } = {
      'Ford Everest': 13,
      'Nissan Terra': 10,
      'Mitsubishi Montero Sport': 8,
      'Toyota Fortuner': 12,
      'Honda CR-V': 9,
      'Mazda CX-5': 7
    };
    const key = `${vehicle.make} ${vehicle.model}`;
    return counts[key] || 5;
  }

  getPriceRange(): string {
    if (!this.vehicle) return '';
    const basePrice = this.vehicle.price;
    const minPrice = basePrice;
    const maxPrice = basePrice + (basePrice * 0.4);
    return `${this.formatPrice(minPrice)} - ${this.formatPrice(maxPrice)}`;
  }

  formatPrice(price: number): string {
    return price.toLocaleString('en-US');
  }

  getFeaturesList(): string[] {
    if (!this.vehicle) return [];
    
    const baseFeatures = [
      'Air Conditioning',
      'Power Steering',
      'Power Windows',
      'Remote Keyless Entry',
      'Touchscreen Infotainment System',
      'Bluetooth Connectivity',
      'USB Ports',
      'Rearview Camera',
      'Anti-lock Braking System (ABS)',
      'Electronic Stability Control',
      'Multiple Airbags',
      'LED Daytime Running Lights'
    ];

    const modelFeatures: { [key: string]: string[] } = {
      'Ford Everest': ['Terrain Management System', 'SYNC 4 Infotainment', '360° Camera', 'Advanced Driver Assistance'],
      'Nissan Terra': ['Intelligent 4WD', 'NissanConnect System', 'Around View Monitor', 'ProPILOT Assist'],
      'Mitsubishi Montero Sport': ['Super Select 4WD-II', 'Rockford Sound System', 'Multi Around Monitor', 'Forward Collision Mitigation'],
      'Toyota Fortuner': ['KDSS Suspension', 'Toyota Safety Sense', 'Multi-terrain Select', 'Premium Audio System'],
      'Honda CR-V': ['Honda Sensing Suite', 'HondaLink', 'Wireless Charging', 'Heated Seats'],
      'Mazda CX-5': ['i-Activsense Safety', 'Mazda Connect', 'G-Vectoring Control', 'Bose Premium Audio']
    };

    const key = `${this.vehicle.make} ${this.vehicle.model}`;
    const specificFeatures = modelFeatures[key] || [];
    
    return [...baseFeatures, ...specificFeatures];
  }

  selectImage(index: number): void {
    this.selectedImageIndex = index;
  }

  toggle360View(): void {
    this.show360View = !this.show360View;
  }

  addToCart(): void {
    if (!this.vehicle) return;
    
    if (this.cartService.isInCart(this.vehicle.id)) {
      alert('This vehicle is already in your cart.');
      return;
    }
    
    if (this.cartService.addToCart(this.vehicle)) {
      alert(`${this.vehicle.year} ${this.vehicle.make} ${this.vehicle.model} added to cart!`);
    } else {
      alert('Failed to add vehicle to cart.');
    }
  }

  addToCompare(): void {
    alert('Added to compare!');
  }

  scheduleTestDrive(): void {
    alert('Test drive scheduled! We will contact you shortly.');
  }
}



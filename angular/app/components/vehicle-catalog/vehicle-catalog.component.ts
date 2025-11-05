import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { Vehicle, VehicleFilter } from '../../models/vehicle.model';
import { VehicleService } from '../../service/vehicle.service';
import { VehicleCompareService } from '../../service/vehicle-compare.service';
import { CartService } from '../../service/cart.service';
import { SoldVehiclesService } from '../../service/sold-vehicles.service';
import { Router } from '@angular/router';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-vehicle-catalog',
  templateUrl: './vehicle-catalog.component.html',
  styleUrls: ['./vehicle-catalog.component.css']
})
export class VehicleCatalogComponent implements OnInit, OnDestroy {
  @ViewChild('carsCarousel') carsCarousel!: ElementRef;
  @ViewChild('usedCarsCarousel') usedCarsCarousel!: ElementRef;

  vehicles: Vehicle[] = [];
  filteredVehicles: Vehicle[] = [];
  usedVehicles: Vehicle[] = [];
  filters: VehicleFilter = {};
  makes: string[] = [];
  models: string[] = [];
  
  selectedBrand: string = 'Toyota';
  
  currentUsedCarSlide: number = 0;
  usedCarsPerView: number = 3;

  currentHeroSlide: number = 0;
  heroSlideInterval: any;
  heroSlides = [
    {
      image: '/assets/img/hd1.png',
      alt: 'Luxury cars showcase'
    },
    {
      image: '/assets/img/hd2.png',
      alt: 'Car dealership'
    },
    {
      image: '/assets/img/hd3.png',
      alt: 'Premium vehicles'
    }
  ];

  popularBrands: string[] = ['Toyota', 'Honda', 'Mitsubishi', 'Subaru', 'Nissan', 'Mazda'];
  
  popularMakes: { name: string; image: string }[] = [
    { name: 'Ford', image: '/assets/img/logo1.jpg' },
    { name: 'Nissan', image: '/assets/img/logo3.png' },
    { name: 'Mitsubishi', image: '/assets/img/logo4.png' },
    { name: 'MG', image: '/assets/img/logo5.png' },
    { name: 'Hyundai', image: '/assets/img/logo2.png' }
  ];
  
  constructor(
    private vehicleService: VehicleService,
    private compareService: VehicleCompareService,
    private cartService: CartService,
    private soldVehiclesService: SoldVehiclesService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadVehicles();
    this.startHeroCarousel();
    
    this.soldVehiclesService.soldVehicles$.subscribe(() => {
      this.loadVehicles();
    });
  }

  ngOnDestroy(): void {
    if (this.heroSlideInterval) {
      clearInterval(this.heroSlideInterval);
    }
  }

  startHeroCarousel(): void {
    this.heroSlideInterval = setInterval(() => {
      this.nextHeroSlide();
    }, 5000); // Change slide every 5 seconds
  }

  nextHeroSlide(): void {
    this.currentHeroSlide = (this.currentHeroSlide + 1) % this.heroSlides.length;
  }

  previousHeroSlide(): void {
    this.currentHeroSlide = (this.currentHeroSlide - 1 + this.heroSlides.length) % this.heroSlides.length;
  }

  goToHeroSlide(index: number): void {
    this.currentHeroSlide = index;
    if (this.heroSlideInterval) {
      clearInterval(this.heroSlideInterval);
    }
    this.startHeroCarousel();
  }

  loadVehicles(): void {
    this.vehicleService.getNewVehicles().pipe(
      catchError(error => {
        console.error('Error loading new vehicles:', error);
        this.vehicles = this.getSampleVehicles();
        this.filteredVehicles = [...this.vehicles];
        this.updateMakesAndModels();
        return of([]);
      })
    ).subscribe((vehicles: Vehicle[]) => {
      if (vehicles && vehicles.length > 0) {
        this.vehicles = vehicles;
        this.filteredVehicles = [...this.vehicles];
        this.updateMakesAndModels();
      } else {
        this.vehicles = this.getSampleVehicles();
        this.filteredVehicles = [...this.vehicles];
        this.updateMakesAndModels();
      }
    });

    this.vehicleService.getUsedVehicles().pipe(
      catchError(error => {
        console.error('Error loading used vehicles:', error);
        this.usedVehicles = this.getSampleUsedVehicles();
        return of([]);
      })
    ).subscribe((vehicles: Vehicle[]) => {
      if (vehicles && vehicles.length > 0) {
        this.usedVehicles = vehicles;
      } else {
        this.usedVehicles = this.getSampleUsedVehicles();
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
        description: 'Ford Everest SUV',
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
        description: 'Nissan Terra SUV',
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
        description: 'Mitsubishi Montero Sport',
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
        description: 'Toyota Fortuner SUV',
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
        description: 'Honda CR-V Crossover',
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
        description: 'Mazda CX-5 Crossover',
        images: ['/assets/img/2024 mazda cx-5.jpg'],
        inStock: true,
        featured: false
      }
    ];
  }

  getPriceRange(vehicle: Vehicle): string {
    // For demo purposes, create a price range
    const basePrice = vehicle.price;
    const minPrice = basePrice;
    const maxPrice = basePrice + (basePrice * 0.4); // 40% more for max
    return `${this.formatPrice(minPrice)} - ${this.formatPrice(maxPrice)}`;
  }

  getSampleUsedVehicles(): Vehicle[] {
    return [
      // Toyota - 2 cars
      {
        id: 4,
        vin: 'JKL012',
        make: 'Toyota',
        model: 'Corolla Altis',
        trim: '1.6V AT',
        year: 2015,
        price: 498000,
        mileage: 80000,
        fuelType: 'Gasoline',
        transmission: 'Automatic',
        color: 'White',
        condition: 'Used',
        description: '2015 Toyota Corolla Altis',
        images: ['/assets/img/2015 Toyota Corolla Altis.jpg'],
        inStock: true,
        featured: false
      },
      {
        id: 5,
        vin: 'MNO345',
        make: 'Toyota',
        model: 'Hiace Commuter',
        trim: 'MT Diesel',
        year: 2020,
        price: 968000,
        mileage: 87000,
        fuelType: 'Diesel',
        transmission: 'Manual',
        color: 'White',
        condition: 'Used',
        description: '2020 Toyota Hiace Commuter',
        images: ['/assets/img/2020 Toyota Hiace Commuter.jpeg'],
        inStock: true,
        featured: false
      },
      // Honda - 2 cars
      {
        id: 10,
        vin: 'ABC987',
        make: 'Honda',
        model: 'City',
        trim: '1.5 E CVT',
        year: 2018,
        price: 625000,
        mileage: 65000,
        fuelType: 'Gasoline',
        transmission: 'CVT',
        color: 'Silver',
        condition: 'Used',
        description: '2018 Honda City',
        images: ['/assets/img/2018 Honda City 1.5 E CVT.jpg'],
        inStock: true,
        featured: false
      },
      {
        id: 16,
        vin: 'HON001',
        make: 'Honda',
        model: 'Civic',
        trim: '1.8 E CVT',
        year: 2019,
        price: 895000,
        mileage: 58000,
        fuelType: 'Gasoline',
        transmission: 'CVT',
        color: 'Blue',
        condition: 'Used',
        description: '2019 Honda Civic',
        images: ['/assets/img/2019 Honda Civic 1.8 E CVT.jpg'],
        inStock: true,
        featured: false
      },
      // Mitsubishi - 2 cars
      {
        id: 11,
        vin: 'DEF654',
        make: 'Mitsubishi',
        model: 'Mirage G4',
        trim: 'GLX CVT',
        year: 2019,
        price: 485000,
        mileage: 55000,
        fuelType: 'Gasoline',
        transmission: 'CVT',
        color: 'Black',
        condition: 'Used',
        description: '2019 Mitsubishi Mirage G4',
        images: ['/assets/img/2019 Mitsubishi Mirage G4 GLX CVT.webp'],
        inStock: true,
        featured: false
      },
      {
        id: 17,
        vin: 'MIT001',
        make: 'Mitsubishi',
        model: 'Montero Sport',
        trim: 'GLS AT',
        year: 2020,
        price: 1450000,
        mileage: 42000,
        fuelType: 'Diesel',
        transmission: 'Automatic',
        color: 'White',
        condition: 'Used',
        description: '2020 Mitsubishi Montero Sport',
        images: ['/assets/img/2020 Mitsubishi Montero Sport GLS AT.jpg'],
        inStock: true,
        featured: false
      },
      // Subaru - 2 cars
      {
        id: 14,
        vin: 'MNO765',
        make: 'Subaru',
        model: 'XV',
        trim: '2.0i-S',
        year: 2018,
        price: 1180000,
        mileage: 68000,
        fuelType: 'Gasoline',
        transmission: 'CVT',
        color: 'Blue',
        condition: 'Used',
        description: '2018 Subaru XV',
        images: ['/assets/img/2018 Subaru XV 2.0i-S'],
        inStock: true,
        featured: false
      },
      {
        id: 18,
        vin: 'SUB001',
        make: 'Subaru',
        model: 'Forester',
        trim: '2.0i-S',
        year: 2019,
        price: 1380000,
        mileage: 52000,
        fuelType: 'Gasoline',
        transmission: 'CVT',
        color: 'Silver',
        condition: 'Used',
        description: '2019 Subaru Forester',
        images: ['/assets/img/2019 Subaru Forester 2.0i-S'],
        inStock: true,
        featured: false
      },
      // Nissan - 2 cars
      {
        id: 12,
        vin: 'GHI321',
        make: 'Nissan',
        model: 'Almera',
        trim: 'VL CVT',
        year: 2020,
        price: 595000,
        mileage: 45000,
        fuelType: 'Gasoline',
        transmission: 'CVT',
        color: 'White',
        condition: 'Used',
        description: '2020 Nissan Almera',
        images: ['/assets/img/2020 Nissan Almera VL CVT.jpg'],
        inStock: true,
        featured: false
      },
      {
        id: 19,
        vin: 'NIS001',
        make: 'Nissan',
        model: 'Terra',
        trim: 'VL 4x2 AT',
        year: 2021,
        price: 1680000,
        mileage: 38000,
        fuelType: 'Diesel',
        transmission: 'Automatic',
        color: 'Black',
        condition: 'Used',
        description: '2021 Nissan Terra',
        images: ['/assets/img/2021 Nissan Terra VL 4x2 AT.webp'],
        inStock: true,
        featured: false
      },
      // Mazda - 2 cars
      {
        id: 13,
        vin: 'JKL098',
        make: 'Mazda',
        model: '3',
        trim: '2.0 R',
        year: 2017,
        price: 728000,
        mileage: 72000,
        fuelType: 'Gasoline',
        transmission: 'Automatic',
        color: 'Red',
        condition: 'Used',
        description: '2017 Mazda 3',
        images: ['/assets/placeholder-car.jpg'],
        inStock: true,
        featured: false
      },
      {
        id: 20,
        vin: 'MAZ001',
        make: 'Mazda',
        model: 'CX-5',
        trim: '2.5 AWD',
        year: 2020,
        price: 1580000,
        mileage: 48000,
        fuelType: 'Gasoline',
        transmission: 'Automatic',
        color: 'White',
        condition: 'Used',
        description: '2020 Mazda CX-5',
        images: ['/assets/img/2024 mazda cx-5.jpg'],
        inStock: true,
        featured: false
      },
      {
        id: 21,
        vin: 'WIG001',
        make: 'Toyota',
        model: 'Wigo',
        year: 2024,
        price: 588000,
        mileage: 20000,
        fuelType: 'Gasoline',
        transmission: 'CVT',
        color: 'White',
        condition: 'Used',
        description: '2024 Toyota Wigo',
        images: ['/assets/img/2024 Toyota Wigo.jpg'],
        inStock: true,
        featured: false
      }
    ];
  }

  updateMakesAndModels(): void {
    this.makes = [...new Set(this.vehicles.map(v => v.make))].sort();
  }


  filterUsedCars(): void {
    const vehiclesToFilter = this.usedVehicles.length > 0 
      ? this.usedVehicles 
      : this.getSampleUsedVehicles();
    
    this.usedVehicles = vehiclesToFilter.filter(v => {
      if (this.selectedBrand && v.make !== this.selectedBrand) return false;
      return true;
    });
  }

  addToCart(vehicle: Vehicle, event: Event): void {
    event.stopPropagation();
    
    if (this.isSold(vehicle.id)) {
      alert('This vehicle has been sold and is no longer available.');
      return;
    }
    
    const vehicleToAdd: Vehicle = {
      id: vehicle.id,
      vin: vehicle.vin,
      make: vehicle.make,
      model: vehicle.model,
      trim: vehicle.trim,
      year: vehicle.year,
      price: vehicle.price,
      mileage: vehicle.mileage,
      fuelType: vehicle.fuelType,
      transmission: vehicle.transmission,
      color: vehicle.color,
      condition: vehicle.condition,
      description: vehicle.description,
      images: vehicle.images || [],
      videoUrl: vehicle.videoUrl,
      threeSixtyUrl: vehicle.threeSixtyUrl,
      inStock: vehicle.inStock,
      featured: vehicle.featured
    };
    
    if (this.cartService.addToCart(vehicleToAdd)) {
      alert(`${vehicle.year} ${vehicle.make} ${vehicle.model} added to cart!`);
    } else {
      alert('This vehicle is already in your cart.');
    }
  }

  isInCart(vehicleId: number): boolean {
    return this.cartService.isInCart(vehicleId);
  }

  isSold(vehicleId: number): boolean {
    return this.soldVehiclesService.isSold(vehicleId);
  }

  formatPrice(price: number): string {
    return price.toLocaleString('en-US');
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

  getSellerLogo(vehicle: Vehicle): string {
    const sellers = ['ALL CARS', 'USED CAR DEALER', 'AUTO MART', 'CAR SHOP', 'PREMIUM AUTO'];
    return sellers[vehicle.id % sellers.length];
  }

  getSellerName(vehicle: Vehicle): string {
    const sellers = [
      'All Cars',
      'Nix Ash Oto Pwesto',
      'Premium Auto Sales',
      'Metro Car Dealers',
      'City Auto Center'
    ];
    return sellers[vehicle.id % sellers.length];
  }

  getSellerLocation(vehicle: Vehicle): string {
    const locations = [
      'Makati City, Metro Manila, NCR',
      'Imus City, Cavite, CALABARZON',
      'Quezon City, Metro Manila, NCR',
      'Mandaluyong City, Metro Manila, NCR',
      'Pasig City, Metro Manila, NCR'
    ];
    return locations[vehicle.id % locations.length];
  }

  scrollCarousel(direction: 'left' | 'right'): void {
    if (this.carsCarousel) {
      // Scroll by card width (320px) + gap (24px) = 344px
      const scrollAmount = 344;
      const currentScroll = this.carsCarousel.nativeElement.scrollLeft;
      const newScroll = direction === 'right' 
        ? currentScroll + scrollAmount 
        : currentScroll - scrollAmount;
      this.carsCarousel.nativeElement.scrollTo({ left: newScroll, behavior: 'smooth' });
    }
  }

  scrollUsedCarsCarousel(direction: 'left' | 'right'): void {
    if (this.usedCarsCarousel) {
      const cardWidth = 320; // Card width + gap
      const gap = 24;
      const scrollAmount = cardWidth + gap;
      const currentScroll = this.usedCarsCarousel.nativeElement.scrollLeft;
      const maxScroll = this.usedCarsCarousel.nativeElement.scrollWidth - this.usedCarsCarousel.nativeElement.clientWidth;
      
      let newScroll: number;
      if (direction === 'right') {
        newScroll = Math.min(currentScroll + scrollAmount, maxScroll);
        this.currentUsedCarSlide = Math.min(
          this.currentUsedCarSlide + 1,
          Math.ceil(this.usedVehicles.length / this.usedCarsPerView) - 1
        );
      } else {
        newScroll = Math.max(currentScroll - scrollAmount, 0);
        this.currentUsedCarSlide = Math.max(this.currentUsedCarSlide - 1, 0);
      }
      
      this.usedCarsCarousel.nativeElement.scrollTo({ left: newScroll, behavior: 'smooth' });
    }
  }

  goToUsedCarSlide(index: number): void {
    if (this.usedCarsCarousel) {
      const cardWidth = 320;
      const gap = 24;
      const scrollAmount = (cardWidth + gap) * index;
      this.usedCarsCarousel.nativeElement.scrollTo({ left: scrollAmount, behavior: 'smooth' });
      this.currentUsedCarSlide = index;
    }
  }

  getUsedCarSlideCount(): number {
    return Math.ceil(this.usedVehicles.length / this.usedCarsPerView);
  }

  getCarouselDotsArray(): number[] {
    return Array(this.getUsedCarSlideCount()).fill(0).map((x, i) => i);
  }

  onFilterChange(): void {
    this.filteredVehicles = this.vehicles.filter(vehicle => {
      if (this.filters.make && vehicle.make !== this.filters.make) return false;
      if (this.filters.model && vehicle.model !== this.filters.model) return false;
      if (this.filters.yearMin && vehicle.year < this.filters.yearMin) return false;
      if (this.filters.yearMax && vehicle.year > this.filters.yearMax) return false;
      if (this.filters.priceMin && vehicle.price < this.filters.priceMin) return false;
      if (this.filters.priceMax && vehicle.price > this.filters.priceMax) return false;
      if (this.filters.mileageMax && vehicle.mileage > this.filters.mileageMax) return false;
      if (this.filters.fuelType && vehicle.fuelType !== this.filters.fuelType) return false;
      if (this.filters.transmission && vehicle.transmission !== this.filters.transmission) return false;
      if (this.filters.condition && vehicle.condition !== this.filters.condition) return false;
      return true;
    });
  }

  clearFilters(): void {
    this.filters = {};
    this.onFilterChange();
  }

  addToCompare(vehicle: Vehicle, event: Event): void {
    event.stopPropagation(); // Prevent navigation when clicking compare button
    
    if (!this.compareService.canAddMore()) {
      alert(`You can only compare up to ${this.compareService.getMaxCompare()} vehicles. Please remove a vehicle from your compare list first.`);
      return;
    }
    
    if (this.compareService.addToCompare(vehicle)) {
      alert(`${vehicle.year} ${vehicle.make} ${vehicle.model} added to compare!`);
    } else {
      if (this.compareService.isInCompareList(vehicle.id)) {
        alert('This vehicle is already in your compare list.');
      }
    }
  }

  isInCompareList(vehicleId: number): boolean {
    return this.compareService.isInCompareList(vehicleId);
  }
}

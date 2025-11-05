import { Component, OnInit, OnDestroy } from '@angular/core';
import { Vehicle } from '../../models/vehicle.model';
import { VehicleCompareService } from '../../service/vehicle-compare.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-vehicle-compare',
  templateUrl: './vehicle-compare.component.html',
  styleUrls: ['./vehicle-compare.component.css']
})
export class VehicleCompareComponent implements OnInit, OnDestroy {
  compareList: Vehicle[] = [];
  maxCompare = 4;
  private subscription?: Subscription;

  constructor(private compareService: VehicleCompareService) { }

  ngOnInit(): void {
    // Load initial list
    this.compareList = this.compareService.getCompareList();
    this.maxCompare = this.compareService.getMaxCompare();
    
    // Subscribe to changes
    this.subscription = this.compareService.compareList$.subscribe(vehicles => {
      this.compareList = vehicles;
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  removeFromCompare(vehicleId: number): void {
    this.compareService.removeFromCompare(vehicleId);
  }

  clearCompare(): void {
    this.compareService.clearCompare();
  }

  getComparisonFeatures(): string[] {
    return ['Price', 'Year', 'Mileage', 'Fuel Type', 'Transmission', 'Condition'];
  }

  formatPrice(price: number): string {
    return price.toLocaleString('en-US');
  }
}



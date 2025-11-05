import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { VehicleService } from './vehicle.service';
import { catchError, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SoldVehiclesService {
  private soldVehiclesSubject = new BehaviorSubject<number[]>([]);
  public soldVehicles$ = this.soldVehiclesSubject.asObservable();
  private readonly STORAGE_KEY = 'sold_vehicles';

  constructor(
    private vehicleService: VehicleService
  ) {
    this.loadSoldVehicles();
  }

  private loadSoldVehicles(): void {
    const stored = localStorage.getItem(this.STORAGE_KEY);
    if (stored) {
      try {
        const soldIds = JSON.parse(stored);
        this.soldVehiclesSubject.next(soldIds);
      } catch (e) {
        localStorage.removeItem(this.STORAGE_KEY);
      }
    }
  }

  private saveSoldVehicles(vehicleIds: number[]): void {
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(vehicleIds));
  }

  markAsSold(vehicleIds: number[]): void {
    const currentSold = this.soldVehiclesSubject.value;
    const newSold = [...new Set([...currentSold, ...vehicleIds])];
    
    // Update local state immediately
    this.soldVehiclesSubject.next(newSold);
    this.saveSoldVehicles(newSold);
    
    // Update backend database
    this.vehicleService.markVehiclesAsSold(vehicleIds).pipe(
      catchError(error => {
        console.error('Error updating vehicle status in database:', error);
        // Still keep the local state even if backend fails
        return of(null);
      })
    ).subscribe(response => {
      if (response) {
        console.log('Vehicles marked as sold in database:', vehicleIds);
      }
    });
  }

  isSold(vehicleId: number): boolean {
    return this.soldVehiclesSubject.value.includes(vehicleId);
  }

  getSoldVehicles(): number[] {
    return this.soldVehiclesSubject.value;
  }
}


import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Vehicle } from '../models/vehicle.model';

@Injectable({
  providedIn: 'root'
})
export class VehicleCompareService {
  private compareListSubject = new BehaviorSubject<Vehicle[]>([]);
  public compareList$ = this.compareListSubject.asObservable();
  private maxCompare = 4;

  constructor() {
    // Load from localStorage on init
    this.loadFromStorage();
  }

  private loadFromStorage(): void {
    const saved = localStorage.getItem('compareVehicles');
    if (saved) {
      try {
        const vehicles = JSON.parse(saved);
        this.compareListSubject.next(vehicles);
      } catch (e) {
        console.error('Error loading compare list from storage:', e);
      }
    }
  }

  private saveToStorage(vehicles: Vehicle[]): void {
    localStorage.setItem('compareVehicles', JSON.stringify(vehicles));
  }

  addToCompare(vehicle: Vehicle): boolean {
    const currentList = this.compareListSubject.value;
    
    if (currentList.length >= this.maxCompare) {
      return false; // Return false if max reached
    }
    
    if (currentList.find(v => v.id === vehicle.id)) {
      return false; // Already in compare list
    }
    
    const newList = [...currentList, vehicle];
    this.compareListSubject.next(newList);
    this.saveToStorage(newList);
    return true;
  }

  removeFromCompare(vehicleId: number): void {
    const newList = this.compareListSubject.value.filter(v => v.id !== vehicleId);
    this.compareListSubject.next(newList);
    this.saveToStorage(newList);
  }

  clearCompare(): void {
    this.compareListSubject.next([]);
    this.saveToStorage([]);
  }

  getCompareList(): Vehicle[] {
    return this.compareListSubject.value;
  }

  isInCompareList(vehicleId: number): boolean {
    return this.compareListSubject.value.some(v => v.id === vehicleId);
  }

  getCompareCount(): number {
    return this.compareListSubject.value.length;
  }

  canAddMore(): boolean {
    return this.compareListSubject.value.length < this.maxCompare;
  }

  getMaxCompare(): number {
    return this.maxCompare;
  }
}


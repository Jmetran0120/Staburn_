import { Injectable } from '@angular/core';
import { BaseHttpService } from './base-http.service';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Vehicle } from '../models/vehicle.model';

@Injectable({
  providedIn: 'root'
})
export class CartService extends BaseHttpService {
  private cartItemsSubject = new BehaviorSubject<Vehicle[]>([]);
  public cartItems$ = this.cartItemsSubject.asObservable();
  private readonly STORAGE_KEY = 'cart_items';

  constructor(protected override http: HttpClient) { 
    super(http, '/api/cart');
    this.loadCartFromStorage();
  }

  private loadCartFromStorage(): void {
    const stored = localStorage.getItem(this.STORAGE_KEY);
    if (stored) {
      try {
        const items = JSON.parse(stored);
        this.cartItemsSubject.next(items);
      } catch (e) {
        localStorage.removeItem(this.STORAGE_KEY);
      }
    }
  }

  private saveToStorage(items: Vehicle[]): void {
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(items));
  }

  addToCart(vehicle: Vehicle): boolean {
    const currentItems = this.cartItemsSubject.value;
    
    // Check if vehicle is already in cart
    if (currentItems.find(v => v.id === vehicle.id)) {
      return false; // Already in cart
    }
    
    const newItems = [...currentItems, vehicle];
    this.cartItemsSubject.next(newItems);
    this.saveToStorage(newItems);
    return true;
  }

  removeFromCart(vehicleId: number): void {
    const newItems = this.cartItemsSubject.value.filter(v => v.id !== vehicleId);
    this.cartItemsSubject.next(newItems);
    this.saveToStorage(newItems);
  }

  clearCart(): void {
    this.cartItemsSubject.next([]);
    localStorage.removeItem(this.STORAGE_KEY);
  }

  getCartItems(): Vehicle[] {
    return this.cartItemsSubject.value;
  }

  getCartCount(): number {
    return this.cartItemsSubject.value.length;
  }

  isInCart(vehicleId: number): boolean {
    return this.cartItemsSubject.value.some(v => v.id === vehicleId);
  }

  getTotalPrice(): number {
    return this.cartItemsSubject.value.reduce((total, vehicle) => total + vehicle.price, 0);
  }
}

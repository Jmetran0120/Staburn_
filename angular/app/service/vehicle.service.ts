import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { BaseHttpService } from './base-http.service';
import { Vehicle } from '../models/vehicle.model';

@Injectable({
  providedIn: 'root'
})
export class VehicleService extends BaseHttpService {

  constructor(protected override http: HttpClient) {
    super(http, '/api/vehicle');
  }

  // Get all vehicles
  public getVehicles(): Observable<Vehicle[]> {
    return this.findAll().pipe(
      // Map backend response to Vehicle[] format
      // Adjust based on your backend response structure
    ) as Observable<Vehicle[]>;
  }

  // Get vehicle by ID
  public getVehicleById(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(this.apiServerUrl + this.path + '/' + id);
  }

  // Get new vehicles
  public getNewVehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(this.apiServerUrl + this.path + '/new');
  }

  // Get used vehicles
  public getUsedVehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(this.apiServerUrl + this.path + '/used');
  }

  // Get vehicles by make
  public getVehiclesByMake(make: string): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(this.apiServerUrl + this.path + '/make/' + make);
  }

  // Search vehicles with filters
  public searchVehicles(filters: any): Observable<Vehicle[]> {
    return this.http.post<Vehicle[]>(this.apiServerUrl + this.path + '/search', filters);
  }

  // Get featured vehicles
  public getFeaturedVehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(this.apiServerUrl + this.path + '/featured');
  }

  // Update vehicle stock status (mark as sold) - individual vehicle
  public updateVehicleStockStatus(vehicleId: number, inStock: boolean): Observable<any> {
    // Try specific endpoint first, fallback to generic update
    return this.http.put<any>(`${this.apiServerUrl}${this.path}/${vehicleId}/stock`, { inStock }).pipe(
      catchError(() => {
        // Fallback: try to update the full vehicle with inStock field
        return this.getVehicleById(vehicleId).pipe(
          switchMap((vehicle: Vehicle) => {
            vehicle.inStock = inStock;
            return this.http.put<any>(`${this.apiServerUrl}${this.path}/${vehicleId}`, vehicle);
          })
        );
      })
    );
  }

  // Mark multiple vehicles as sold
  public markVehiclesAsSold(vehicleIds: number[]): Observable<any> {
    // Try batch endpoint first
    return this.http.post<any>(`${this.apiServerUrl}${this.path}/mark-sold`, { vehicleIds }).pipe(
      catchError(() => {
        // Fallback: update each vehicle individually
        const updateObservables = vehicleIds.map(id => 
          this.updateVehicleStockStatus(id, false)
        );
        return forkJoin(updateObservables);
      })
    );
  }
}


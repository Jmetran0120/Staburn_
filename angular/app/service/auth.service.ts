import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

export interface User {
  id: number;
  email: string;
  name: string;
  role: 'admin' | 'customer';
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private apiServerUrl = environment.apiBaseUrl;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    // Check for existing session on service initialization
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      try {
        const user = JSON.parse(storedUser);
        this.currentUserSubject.next(user);
      } catch (e) {
        localStorage.removeItem('currentUser');
      }
    }
  }

  get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiServerUrl}/api/auth/login`, {
      email,
      password
    }).pipe(
      // Using RxJS map operator to handle response
      map((response: any) => {
        if (response && response.user) {
          const user: User = {
            id: response.user.id,
            email: response.user.email,
            name: response.user.name || response.user.email,
            role: response.user.role || 'customer'
          };
          localStorage.setItem('currentUser', JSON.stringify(user));
          localStorage.setItem('token', response.token || '');
          this.currentUserSubject.next(user);
          return user;
        }
        throw new Error('Invalid response from server');
      })
    );
  }

  signup(email: string, password: string, name: string, role: 'admin' | 'customer' = 'customer'): Observable<any> {
    return this.http.post<any>(`${this.apiServerUrl}/api/auth/signup`, {
      email,
      password,
      name,
      role
    }).pipe(
      map((response: any) => {
        if (response && response.user) {
          const user: User = {
            id: response.user.id,
            email: response.user.email,
            name: response.user.name || response.user.email,
            role: response.user.role || 'customer'
          };
          localStorage.setItem('currentUser', JSON.stringify(user));
          localStorage.setItem('token', response.token || '');
          this.currentUserSubject.next(user);
          return user;
        }
        throw new Error('Invalid response from server');
      })
    );
  }

  logout() {
    localStorage.removeItem('currentUser');
    localStorage.removeItem('token');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return !!this.currentUserSubject.value;
  }

  isAdmin(): boolean {
    return this.currentUserSubject.value?.role === 'admin';
  }

  isCustomer(): boolean {
    return this.currentUserSubject.value?.role === 'customer';
  }
}


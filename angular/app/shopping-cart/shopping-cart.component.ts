import { Component, OnInit } from '@angular/core';
import { CartService } from '../service/cart.service';
import { SoldVehiclesService } from '../service/sold-vehicles.service';
import { OrderService } from '../service/order.service';
import { AuthService } from '../service/auth.service';
import { Vehicle } from '../models/vehicle.model';
import { Router } from '@angular/router';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {
  cartItems: Vehicle[] = [];
  totalPrice: number = 0;

  checkoutForm = {
    customerName: '',
    email: '',
    phone: '',
    address: '',
    paymentMethod: 'Cash'
  };
  isCheckingOut = false;

  constructor(
    private cartService: CartService,
    private soldVehiclesService: SoldVehiclesService,
    private orderService: OrderService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCartItems();
    this.cartService.cartItems$.subscribe(items => {
      this.cartItems = items;
      this.totalPrice = this.cartService.getTotalPrice();
    });
    
    const currentUser = this.authService.currentUserValue;
    if (currentUser) {
      this.checkoutForm.customerName = currentUser.name;
      this.checkoutForm.email = currentUser.email;
    }
  }

  loadCartItems(): void {
    this.cartItems = this.cartService.getCartItems();
    this.totalPrice = this.cartService.getTotalPrice();
  }

  removeItem(vehicleId: number): void {
    this.cartService.removeFromCart(vehicleId);
  }

  clearCart(): void {
    if (confirm('Are you sure you want to clear your cart?')) {
      this.cartService.clearCart();
    }
  }

  formatPrice(price: number): string {
    return price.toLocaleString('en-US');
  }

  continueShopping(): void {
    this.router.navigate(['/']);
  }

  checkout(): void {
    if (this.cartItems.length === 0) {
      alert('Your cart is empty!');
      return;
    }

    if (!this.checkoutForm.customerName || !this.checkoutForm.email || !this.checkoutForm.phone || !this.checkoutForm.address) {
      alert('Please fill in all required fields');
      return;
    }

    if (!confirm('Are you sure you want to proceed with checkout?')) {
      return;
    }

    this.isCheckingOut = true;

    const orderData = {
      customer_id: this.authService.currentUserValue?.id || 0,
      customer_name: this.checkoutForm.customerName,
      status: 'PAID',
      total_amount: this.totalPrice,
      shipping_address: this.checkoutForm.address,
      payment_method: this.checkoutForm.paymentMethod,
      notes: `Phone: ${this.checkoutForm.phone}, Email: ${this.checkoutForm.email}`
    };

    const vehicleIds = this.cartItems.map(item => item.id);

    // First, mark vehicles as sold in the database
    this.orderService.add(orderData).pipe(
      catchError(error => {
        console.error('Error creating order:', error);
        // Continue with local processing even if order creation fails
        return of(null);
      })
    ).subscribe(response => {
      if (response) {
        console.log('Order created:', response);
      }
      
      // Process order locally (this will also try to update database via SoldVehiclesService)
      this.processOrderLocally();
    });
  }

  private processOrderLocally(): void {
    const vehicleCount = this.cartItems.length;
    const finalTotal = this.totalPrice;
    const vehicleIds = this.cartItems.map(item => item.id);

    this.soldVehiclesService.markAsSold(vehicleIds);

    this.cartService.clearCart();

    alert(`Order successful! ${vehicleCount} vehicle(s) have been purchased.\n\nTotal: ₱${this.formatPrice(finalTotal)}`);

    this.isCheckingOut = false;

    this.router.navigate(['/']);
  }
}

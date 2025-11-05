import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  name = '';
  email = '';
  password = '';
  confirmPassword = '';
  role: 'admin' | 'customer' = 'customer';
  error = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    // Redirect if already logged in
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/']);
    }
  }

  onSubmit() {
    if (!this.name || !this.email || !this.password || !this.confirmPassword) {
      this.error = 'Please fill in all fields';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.error = 'Passwords do not match';
      return;
    }

    if (this.password.length < 6) {
      this.error = 'Password must be at least 6 characters long';
      return;
    }

    this.loading = true;
    this.error = '';

    this.authService.signup(this.email, this.password, this.name, this.role).subscribe({
      next: (user) => {
        this.loading = false;
        // Redirect based on user role
        if (user.role === 'admin') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/']);
        }
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Sign up failed. Please try again.';
      }
    });
  }
}


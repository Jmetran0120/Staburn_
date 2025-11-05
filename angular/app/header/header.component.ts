import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  isAuthenticated = false;
  currentUser: any = null;
  showUserMenu = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      this.isAuthenticated = !!user;
    });
  }

  @HostListener('document:click', ['$event'])
  onClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (!target.closest('.user-menu')) {
      this.showUserMenu = false;
    }
  }

  toggleUserMenu() {
    this.showUserMenu = !this.showUserMenu;
  }

  logout() {
    this.authService.logout();
    this.showUserMenu = false;
    this.router.navigate(['/']);
  }

  scrollToSection(sectionId: string, event: Event) {
    event.preventDefault();
    const currentUrl = this.router.url;
    
    // If we're not on the homepage or inventory page, navigate to homepage first
    if (currentUrl !== '/' && currentUrl !== '/inventory') {
      this.router.navigate(['/']).then(() => {
        setTimeout(() => this.scrollToElement(sectionId), 200);
      });
    } else {
      // If already on the page, scroll immediately
      setTimeout(() => this.scrollToElement(sectionId), 50);
    }
  }

  private scrollToElement(sectionId: string) {
    const element = document.getElementById(sectionId);
    const headerElement = document.querySelector('.header') as HTMLElement;
    
    if (element) {
      // Dynamically get header height to ensure it stays visible
      const headerHeight = headerElement ? headerElement.offsetHeight : 80;
      const headerOffset = headerHeight + 20; // Add 20px extra padding for better visibility
      
      const elementPosition = element.getBoundingClientRect().top;
      const offsetPosition = elementPosition + window.pageYOffset - headerOffset;

      window.scrollTo({
        top: offsetPosition,
        behavior: 'smooth'
      });
    }
  }
}

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { MainHeaderComponent } from './main-header/main-header.component';
import { HttpClientModule } from '@angular/common/http';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { CustomerServiceComponent } from './customer-service/customer-service.component';
import { CompanyHomeComponent } from './company-home/company-home.component';
import { ContactUsComponent } from './contact-us/contact-us.component';
import { VehicleCatalogComponent } from './components/vehicle-catalog/vehicle-catalog.component';
import { VehicleCompareComponent } from './components/vehicle-compare/vehicle-compare.component';
import { VehicleDetailComponent } from './components/vehicle-detail/vehicle-detail.component';
import { LoginComponent } from './auth/login/login.component';
import { SignupComponent } from './auth/signup/signup.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    MainHeaderComponent,
    ShoppingCartComponent,
    CustomerServiceComponent,
    CompanyHomeComponent,
    ContactUsComponent,
    VehicleCatalogComponent,
    VehicleCompareComponent,
    VehicleDetailComponent,
    LoginComponent,
    SignupComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

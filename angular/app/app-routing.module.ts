import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VehicleCatalogComponent } from './components/vehicle-catalog/vehicle-catalog.component';
import { VehicleCompareComponent } from './components/vehicle-compare/vehicle-compare.component';
import { VehicleDetailComponent } from './components/vehicle-detail/vehicle-detail.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { ContactUsComponent } from './contact-us/contact-us.component';
import { LoginComponent } from './auth/login/login.component';
import { SignupComponent } from './auth/signup/signup.component';

const routes: Routes = [
  {path: '', component: VehicleCatalogComponent}, 
  {path: 'inventory', component: VehicleCatalogComponent},
  {path: 'compare', component: VehicleCompareComponent},
  {path: 'vehicle/:id', component: VehicleDetailComponent},
  {path: 'accessories', component: ShoppingCartComponent}, // TODO: Create AccessoriesComponent
  {path: 'blog', component: VehicleCatalogComponent}, // TODO: Create BlogComponent
  {path: 'cart', component: ShoppingCartComponent}, 
  {path: 'contact', component: ContactUsComponent},
  {path: 'login', component: LoginComponent},
  {path: 'signup', component: SignupComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

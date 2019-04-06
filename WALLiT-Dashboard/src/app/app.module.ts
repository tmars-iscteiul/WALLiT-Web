import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DataService } from './data.service'

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChartsModule } from 'ng2-charts';
import { LoginPageComponent } from './login-page/login-page.component';
import { FundDashboardPageComponent } from './fund-dashboard-page/fund-dashboard-page.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  { path: 'dashboard', component: FundDashboardPageComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    FundDashboardPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ChartsModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule],
  providers: [DataService],
  bootstrap: [AppComponent]
})
export class AppModule { }

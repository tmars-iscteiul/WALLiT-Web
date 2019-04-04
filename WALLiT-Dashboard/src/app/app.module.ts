import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChartsModule } from 'ng2-charts';
import { LoginPageComponent } from './login-page/login-page.component';
import { FundDashboardPageComponent } from './fund-dashboard-page/fund-dashboard-page.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    FundDashboardPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ChartsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

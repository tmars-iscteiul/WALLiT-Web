import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DataService } from './data.service'
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChartsModule } from 'ng2-charts';
import { LoginPageComponent } from './login-page/login-page.component';
import { FundDashboardPageComponent } from './fund-dashboard-page/fund-dashboard-page.component';
import { HomePageComponent } from './home-page/home-page.component';
import { PersonalPageComponent } from './personal-page/personal-page.component';
import { AboutWallitComponent } from './about-wallit/about-wallit.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomePageComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'personal', component: PersonalPageComponent },
  { path: 'dashboard', component: FundDashboardPageComponent },
  { path: 'about', component: AboutWallitComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    FundDashboardPageComponent,
    HomePageComponent,
    PersonalPageComponent,
    AboutWallitComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ChartsModule,
    RouterModule.forRoot(routes),
    NgbModule,
    FormsModule
  ],
  exports: [RouterModule],
  providers: [DataService],
  bootstrap: [AppComponent]
})
export class AppModule { }

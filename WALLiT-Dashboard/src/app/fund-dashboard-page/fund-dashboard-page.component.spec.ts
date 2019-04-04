import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FundDashboardPageComponent } from './fund-dashboard-page.component';

describe('FundDashboardPageComponent', () => {
  let component: FundDashboardPageComponent;
  let fixture: ComponentFixture<FundDashboardPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FundDashboardPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FundDashboardPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

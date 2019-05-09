import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AboutWallitComponent } from './about-wallit.component';

describe('AboutWallitComponent', () => {
  let component: AboutWallitComponent;
  let fixture: ComponentFixture<AboutWallitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AboutWallitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AboutWallitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

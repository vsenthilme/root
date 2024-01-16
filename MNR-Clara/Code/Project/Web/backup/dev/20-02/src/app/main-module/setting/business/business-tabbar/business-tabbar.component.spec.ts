import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BusinessTabbarComponent } from './business-tabbar.component';

describe('BusinessTabbarComponent', () => {
  let component: BusinessTabbarComponent;
  let fixture: ComponentFixture<BusinessTabbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BusinessTabbarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BusinessTabbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

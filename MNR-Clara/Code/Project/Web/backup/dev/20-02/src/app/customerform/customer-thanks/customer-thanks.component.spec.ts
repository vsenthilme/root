import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerThanksComponent } from './customer-thanks.component';

describe('CustomerThanksComponent', () => {
  let component: CustomerThanksComponent;
  let fixture: ComponentFixture<CustomerThanksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CustomerThanksComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerThanksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

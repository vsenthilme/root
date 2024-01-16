import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentplanDetailsComponent } from './paymentplan-details.component';

describe('PaymentplanDetailsComponent', () => {
  let component: PaymentplanDetailsComponent;
  let fixture: ComponentFixture<PaymentplanDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PaymentplanDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymentplanDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

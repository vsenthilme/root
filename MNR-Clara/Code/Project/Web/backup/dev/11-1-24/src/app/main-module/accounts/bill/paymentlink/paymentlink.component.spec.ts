import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentlinkComponent } from './paymentlink.component';

describe('PaymentlinkComponent', () => {
  let component: PaymentlinkComponent;
  let fixture: ComponentFixture<PaymentlinkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PaymentlinkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymentlinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

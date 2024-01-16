import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentplanNewComponent } from './paymentplan-new.component';

describe('PaymentplanNewComponent', () => {
  let component: PaymentplanNewComponent;
  let fixture: ComponentFixture<PaymentplanNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PaymentplanNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymentplanNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

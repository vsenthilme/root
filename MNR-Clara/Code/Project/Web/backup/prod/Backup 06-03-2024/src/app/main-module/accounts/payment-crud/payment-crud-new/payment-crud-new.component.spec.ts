import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentCrudNewComponent } from './payment-crud-new.component';

describe('PaymentCrudNewComponent', () => {
  let component: PaymentCrudNewComponent;
  let fixture: ComponentFixture<PaymentCrudNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PaymentCrudNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymentCrudNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

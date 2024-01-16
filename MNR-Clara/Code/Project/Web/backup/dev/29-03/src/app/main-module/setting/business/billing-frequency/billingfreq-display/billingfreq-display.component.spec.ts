import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BillingfreqDisplayComponent } from './billingfreq-display.component';

describe('BillingfreqDisplayComponent', () => {
  let component: BillingfreqDisplayComponent;
  let fixture: ComponentFixture<BillingfreqDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BillingfreqDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BillingfreqDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

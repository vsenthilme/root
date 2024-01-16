import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BillingformatDisplayComponent } from './billingformat-display.component';

describe('BillingformatDisplayComponent', () => {
  let component: BillingformatDisplayComponent;
  let fixture: ComponentFixture<BillingformatDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BillingformatDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BillingformatDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

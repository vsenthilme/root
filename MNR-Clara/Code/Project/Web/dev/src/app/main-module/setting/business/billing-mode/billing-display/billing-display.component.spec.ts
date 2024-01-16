import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BillingDisplayComponent } from './billing-display.component';

describe('BillingDisplayComponent', () => {
  let component: BillingDisplayComponent;
  let fixture: ComponentFixture<BillingDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BillingDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BillingDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BillingModeComponent } from './billing-mode.component';

describe('BillingModeComponent', () => {
  let component: BillingModeComponent;
  let fixture: ComponentFixture<BillingModeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BillingModeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BillingModeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

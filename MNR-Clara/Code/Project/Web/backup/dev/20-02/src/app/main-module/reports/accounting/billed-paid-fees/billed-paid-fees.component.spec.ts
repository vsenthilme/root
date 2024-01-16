import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BilledPaidFeesComponent } from './billed-paid-fees.component';

describe('BilledPaidFeesComponent', () => {
  let component: BilledPaidFeesComponent;
  let fixture: ComponentFixture<BilledPaidFeesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BilledPaidFeesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BilledPaidFeesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

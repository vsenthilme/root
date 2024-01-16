import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BilledPaidComponent } from './billed-paid.component';

describe('BilledPaidComponent', () => {
  let component: BilledPaidComponent;
  let fixture: ComponentFixture<BilledPaidComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BilledPaidComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BilledPaidComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

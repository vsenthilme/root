import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BillingFormatComponent } from './billing-format.component';

describe('BillingFormatComponent', () => {
  let component: BillingFormatComponent;
  let fixture: ComponentFixture<BillingFormatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BillingFormatComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BillingFormatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

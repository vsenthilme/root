import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BilledHrsPartnerComponent } from './billed-hrs-partner.component';

describe('BilledHrsPartnerComponent', () => {
  let component: BilledHrsPartnerComponent;
  let fixture: ComponentFixture<BilledHrsPartnerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BilledHrsPartnerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BilledHrsPartnerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

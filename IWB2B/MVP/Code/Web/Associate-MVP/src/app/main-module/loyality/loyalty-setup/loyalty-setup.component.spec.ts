import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoyaltySetupComponent } from './loyalty-setup.component';

describe('LoyaltySetupComponent', () => {
  let component: LoyaltySetupComponent;
  let fixture: ComponentFixture<LoyaltySetupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoyaltySetupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoyaltySetupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

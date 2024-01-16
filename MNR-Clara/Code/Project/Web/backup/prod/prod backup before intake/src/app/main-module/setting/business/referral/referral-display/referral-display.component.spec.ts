import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReferralDisplayComponent } from './referral-display.component';

describe('ReferralDisplayComponent', () => {
  let component: ReferralDisplayComponent;
  let fixture: ComponentFixture<ReferralDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReferralDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReferralDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

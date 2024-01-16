import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReferralOutputComponent } from './referral-output.component';

describe('ReferralOutputComponent', () => {
  let component: ReferralOutputComponent;
  let fixture: ComponentFixture<ReferralOutputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReferralOutputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReferralOutputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

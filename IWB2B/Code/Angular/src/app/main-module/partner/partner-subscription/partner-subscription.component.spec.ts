import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartnerSubscriptionComponent } from './partner-subscription.component';

describe('PartnerSubscriptionComponent', () => {
  let component: PartnerSubscriptionComponent;
  let fixture: ComponentFixture<PartnerSubscriptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartnerSubscriptionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PartnerSubscriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

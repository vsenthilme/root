import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubscriptionNewComponent } from './subscription-new.component';

describe('SubscriptionNewComponent', () => {
  let component: SubscriptionNewComponent;
  let fixture: ComponentFixture<SubscriptionNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubscriptionNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubscriptionNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeticketNotificationComponent } from './timeticket-notification.component';

describe('TimeticketNotificationComponent', () => {
  let component: TimeticketNotificationComponent;
  let fixture: ComponentFixture<TimeticketNotificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TimeticketNotificationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TimeticketNotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

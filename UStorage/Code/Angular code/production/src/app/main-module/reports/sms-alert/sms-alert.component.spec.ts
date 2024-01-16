import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmsAlertComponent } from './sms-alert.component';

describe('SmsAlertComponent', () => {
  let component: SmsAlertComponent;
  let fixture: ComponentFixture<SmsAlertComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SmsAlertComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SmsAlertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SendEmailApprovalComponent } from './send-email-approval.component';

describe('SendEmailApprovalComponent', () => {
  let component: SendEmailApprovalComponent;
  let fixture: ComponentFixture<SendEmailApprovalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SendEmailApprovalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SendEmailApprovalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

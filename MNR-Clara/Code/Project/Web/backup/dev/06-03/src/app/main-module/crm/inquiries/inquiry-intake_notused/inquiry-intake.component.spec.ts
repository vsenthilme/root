import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InquiryIntakeComponent } from './inquiry-intake.component';

describe('InquiryIntakeComponent', () => {
  let component: InquiryIntakeComponent;
  let fixture: ComponentFixture<InquiryIntakeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InquiryIntakeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InquiryIntakeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InquiryModeComponent } from './inquiry-mode.component';

describe('InquiryModeComponent', () => {
  let component: InquiryModeComponent;
  let fixture: ComponentFixture<InquiryModeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InquiryModeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InquiryModeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InquiryUpdate3Component } from './inquiry-update3.component';

describe('InquiryUpdate3Component', () => {
  let component: InquiryUpdate3Component;
  let fixture: ComponentFixture<InquiryUpdate3Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InquiryUpdate3Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InquiryUpdate3Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InquiryStatusComponent } from './inquiry-status.component';

describe('InquiryStatusComponent', () => {
  let component: InquiryStatusComponent;
  let fixture: ComponentFixture<InquiryStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InquiryStatusComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InquiryStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

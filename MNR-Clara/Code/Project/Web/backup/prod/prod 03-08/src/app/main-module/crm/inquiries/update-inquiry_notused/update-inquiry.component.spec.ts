import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateInquiryComponent } from './update-inquiry.component';

describe('UpdateInquiryComponent', () => {
  let component: UpdateInquiryComponent;
  let fixture: ComponentFixture<UpdateInquiryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateInquiryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateInquiryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InquiryAllocateComponent } from './inquiry-allocate.component';

describe('InquiryAllocateComponent', () => {
  let component: InquiryAllocateComponent;
  let fixture: ComponentFixture<InquiryAllocateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InquiryAllocateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InquiryAllocateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

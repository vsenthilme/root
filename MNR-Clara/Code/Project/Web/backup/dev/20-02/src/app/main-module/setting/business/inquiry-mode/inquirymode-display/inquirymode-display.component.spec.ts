import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InquirymodeDisplayComponent } from './inquirymode-display.component';

describe('InquirymodeDisplayComponent', () => {
  let component: InquirymodeDisplayComponent;
  let fixture: ComponentFixture<InquirymodeDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InquirymodeDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InquirymodeDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InquirySearchComponent } from './inquiry-search.component';

describe('InquirySearchComponent', () => {
  let component: InquirySearchComponent;
  let fixture: ComponentFixture<InquirySearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InquirySearchComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InquirySearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

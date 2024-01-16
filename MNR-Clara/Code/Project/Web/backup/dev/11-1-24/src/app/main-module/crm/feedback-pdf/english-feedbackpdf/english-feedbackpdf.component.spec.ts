import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnglishFeedbackpdfComponent } from './english-feedbackpdf.component';

describe('EnglishFeedbackpdfComponent', () => {
  let component: EnglishFeedbackpdfComponent;
  let fixture: ComponentFixture<EnglishFeedbackpdfComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EnglishFeedbackpdfComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EnglishFeedbackpdfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

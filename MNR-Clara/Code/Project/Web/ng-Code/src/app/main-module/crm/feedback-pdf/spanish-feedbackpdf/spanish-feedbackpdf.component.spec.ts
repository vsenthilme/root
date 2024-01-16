import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpanishFeedbackpdfComponent } from './spanish-feedbackpdf.component';

describe('SpanishFeedbackpdfComponent', () => {
  let component: SpanishFeedbackpdfComponent;
  let fixture: ComponentFixture<SpanishFeedbackpdfComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SpanishFeedbackpdfComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SpanishFeedbackpdfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

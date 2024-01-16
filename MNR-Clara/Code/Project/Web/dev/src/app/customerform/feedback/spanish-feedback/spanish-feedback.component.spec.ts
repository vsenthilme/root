import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SpanishFeedbackComponent } from './spanish-feedback.component';

describe('SpanishFeedbackComponent', () => {
  let component: SpanishFeedbackComponent;
  let fixture: ComponentFixture<SpanishFeedbackComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SpanishFeedbackComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SpanishFeedbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

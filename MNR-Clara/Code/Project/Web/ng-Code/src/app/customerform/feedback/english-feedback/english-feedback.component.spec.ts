import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnglishFeedbackComponent } from './english-feedback.component';

describe('EnglishFeedbackComponent', () => {
  let component: EnglishFeedbackComponent;
  let fixture: ComponentFixture<EnglishFeedbackComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EnglishFeedbackComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EnglishFeedbackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

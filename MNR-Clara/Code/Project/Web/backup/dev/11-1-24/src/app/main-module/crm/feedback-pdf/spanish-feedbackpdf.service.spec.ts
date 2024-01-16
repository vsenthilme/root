import { TestBed } from '@angular/core/testing';

import { SpanishFeedbackpdfService } from './spanish-feedbackpdf.service';

describe('SpanishFeedbackpdfService', () => {
  let service: SpanishFeedbackpdfService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpanishFeedbackpdfService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

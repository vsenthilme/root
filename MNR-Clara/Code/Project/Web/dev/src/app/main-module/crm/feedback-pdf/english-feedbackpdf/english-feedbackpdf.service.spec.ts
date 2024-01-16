import { TestBed } from '@angular/core/testing';

import { EnglishFeedbackpdfService } from './english-feedbackpdf.service';

describe('EnglishFeedbackpdfService', () => {
  let service: EnglishFeedbackpdfService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EnglishFeedbackpdfService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

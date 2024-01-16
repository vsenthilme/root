import { TestBed } from '@angular/core/testing';

import { FeedbackpdfService } from './feedbackpdf.service';

describe('FeedbackpdfService', () => {
  let service: FeedbackpdfService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedbackpdfService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

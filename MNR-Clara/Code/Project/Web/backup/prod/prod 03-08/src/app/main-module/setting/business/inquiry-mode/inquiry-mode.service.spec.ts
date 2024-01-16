import { TestBed } from '@angular/core/testing';

import { InquiryModeService } from './inquiry-mode.service';

describe('InquiryModeService', () => {
  let service: InquiryModeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InquiryModeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

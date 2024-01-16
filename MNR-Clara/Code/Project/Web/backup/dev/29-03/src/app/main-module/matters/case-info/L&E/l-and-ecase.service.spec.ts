import { TestBed } from '@angular/core/testing';

import { LAndECaseService } from './l-and-ecase.service';

describe('LAndECaseService', () => {
  let service: LAndECaseService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LAndECaseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

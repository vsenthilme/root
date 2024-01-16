import { TestBed } from '@angular/core/testing';

import { LAndEService } from './l-and-e.service';

describe('LAndEService', () => {
  let service: LAndEService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LAndEService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

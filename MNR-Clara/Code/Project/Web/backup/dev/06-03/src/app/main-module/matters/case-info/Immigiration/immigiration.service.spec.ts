import { TestBed } from '@angular/core/testing';

import { ImmigirationService } from './immigiration.service';

describe('ImmigirationService', () => {
  let service: ImmigirationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImmigirationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

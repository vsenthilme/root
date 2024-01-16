import { TestBed } from '@angular/core/testing';

import { HandlingChargesService } from './handling-charges.service';

describe('HandlingChargesService', () => {
  let service: HandlingChargesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HandlingChargesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

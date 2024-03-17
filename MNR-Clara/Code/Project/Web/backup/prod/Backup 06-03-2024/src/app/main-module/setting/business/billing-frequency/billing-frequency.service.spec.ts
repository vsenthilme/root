import { TestBed } from '@angular/core/testing';

import { BillingFrequencyService } from './billing-frequency.service';

describe('BillingFrequencyService', () => {
  let service: BillingFrequencyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BillingFrequencyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

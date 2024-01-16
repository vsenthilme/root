import { TestBed } from '@angular/core/testing';

import { BillingModeService } from './billing-mode.service';

describe('BillingModeService', () => {
  let service: BillingModeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BillingModeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

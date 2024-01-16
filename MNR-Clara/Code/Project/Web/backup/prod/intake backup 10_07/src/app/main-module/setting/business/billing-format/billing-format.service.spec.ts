import { TestBed } from '@angular/core/testing';

import { BillingFormatService } from './billing-format.service';

describe('BillingFormatService', () => {
  let service: BillingFormatService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BillingFormatService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

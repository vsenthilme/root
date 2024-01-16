import { TestBed } from '@angular/core/testing';

import { PaymentTermService } from './payment-term.service';

describe('PaymentTermService', () => {
  let service: PaymentTermService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaymentTermService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

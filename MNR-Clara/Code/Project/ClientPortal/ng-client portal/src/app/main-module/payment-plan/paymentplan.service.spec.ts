import { TestBed } from '@angular/core/testing';

import { PaymentplanService } from './paymentplan.service';

describe('PaymentplanService', () => {
  let service: PaymentplanService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaymentplanService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

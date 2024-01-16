import { TestBed } from '@angular/core/testing';

import { ReceiptNoService } from './receipt-no.service';

describe('ReceiptNoService', () => {
  let service: ReceiptNoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReceiptNoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

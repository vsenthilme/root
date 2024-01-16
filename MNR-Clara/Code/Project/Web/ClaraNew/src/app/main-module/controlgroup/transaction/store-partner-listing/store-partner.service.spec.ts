import { TestBed } from '@angular/core/testing';

import { StorePartnerService } from './store-partner.service';

describe('StorePartnerService', () => {
  let service: StorePartnerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StorePartnerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

import { TestBed } from '@angular/core/testing';

import { ExpirationDateService } from './expiration-date.service';

describe('ExpirationDateService', () => {
  let service: ExpirationDateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpirationDateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

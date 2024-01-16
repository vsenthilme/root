import { TestBed } from '@angular/core/testing';

import { PrebillService } from './prebill.service';

describe('PrebillService', () => {
  let service: PrebillService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrebillService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

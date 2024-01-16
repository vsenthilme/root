import { TestBed } from '@angular/core/testing';

import { FlRentService } from './fl-rent.service';

describe('FlRentService', () => {
  let service: FlRentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FlRentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

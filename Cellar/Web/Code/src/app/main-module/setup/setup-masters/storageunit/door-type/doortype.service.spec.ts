import { TestBed } from '@angular/core/testing';

import { DoortypeService } from './doortype.service';

describe('DoortypeService', () => {
  let service: DoortypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DoortypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

import { TestBed } from '@angular/core/testing';

import { CilentCatgService } from './cilent-catg.service';

describe('CilentCatgService', () => {
  let service: CilentCatgService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CilentCatgService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

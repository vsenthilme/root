import { TestBed } from '@angular/core/testing';

import { PotentialService } from './potential.service';

describe('PotentialService', () => {
  let service: PotentialService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PotentialService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

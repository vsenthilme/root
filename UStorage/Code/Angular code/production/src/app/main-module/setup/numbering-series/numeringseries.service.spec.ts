import { TestBed } from '@angular/core/testing';

import { NumeringseriesService } from './numeringseries.service';

describe('NumeringseriesService', () => {
  let service: NumeringseriesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NumeringseriesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

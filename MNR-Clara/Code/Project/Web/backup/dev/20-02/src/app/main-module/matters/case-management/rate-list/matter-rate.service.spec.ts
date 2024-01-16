import { TestBed } from '@angular/core/testing';

import { MatterRateService } from './matter-rate.service';

describe('MatterRateService', () => {
  let service: MatterRateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MatterRateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

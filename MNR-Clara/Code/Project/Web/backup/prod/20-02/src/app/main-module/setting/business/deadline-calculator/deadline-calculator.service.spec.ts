import { TestBed } from '@angular/core/testing';

import { DeadlineCalculatorService } from './deadline-calculator.service';

describe('DeadlineCalculatorService', () => {
  let service: DeadlineCalculatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeadlineCalculatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

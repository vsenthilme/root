import { TestBed } from '@angular/core/testing';
import { NumberRangeService } from './number-range.service';

describe('NumberRangeService', () => {
  let service: NumberRangeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NumberRangeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

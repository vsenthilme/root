import { TestBed } from '@angular/core/testing';

import { NumberrangecodeService } from './numberrangecode.service';

describe('NumberrangecodeService', () => {
  let service: NumberrangecodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NumberrangecodeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

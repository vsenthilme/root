import { TestBed } from '@angular/core/testing';

import { ControlgrouptypeService } from './controlgrouptype.service';

describe('ControlgrouptypeService', () => {
  let service: ControlgrouptypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ControlgrouptypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

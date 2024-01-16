import { TestBed } from '@angular/core/testing';

import { ControlgroupService } from './controlgroup.service';

describe('ControlgroupService', () => {
  let service: ControlgroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ControlgroupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

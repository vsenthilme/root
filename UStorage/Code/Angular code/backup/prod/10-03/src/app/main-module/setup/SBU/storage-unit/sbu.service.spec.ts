import { TestBed } from '@angular/core/testing';

import { SbuService } from './sbu.service';

describe('SbuService', () => {
  let service: SbuService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SbuService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

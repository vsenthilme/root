import { TestBed } from '@angular/core/testing';

import { GeneralMatterService } from './general-matter.service';

describe('GeneralMatterService', () => {
  let service: GeneralMatterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GeneralMatterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

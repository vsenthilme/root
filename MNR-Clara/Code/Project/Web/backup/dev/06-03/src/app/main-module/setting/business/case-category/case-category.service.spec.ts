import { TestBed } from '@angular/core/testing';

import { CaseCategoryService } from './case-category.service';

describe('CaseCategoryService', () => {
  let service: CaseCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CaseCategoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

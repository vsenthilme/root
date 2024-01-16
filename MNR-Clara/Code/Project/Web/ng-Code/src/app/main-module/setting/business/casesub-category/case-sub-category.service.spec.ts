import { TestBed } from '@angular/core/testing';

import { CaseSubCategoryService } from './case-sub-category.service';

describe('CaseSubCategoryService', () => {
  let service: CaseSubCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CaseSubCategoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

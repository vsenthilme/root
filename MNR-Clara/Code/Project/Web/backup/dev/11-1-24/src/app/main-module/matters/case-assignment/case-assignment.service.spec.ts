import { TestBed } from '@angular/core/testing';

import { CaseAssignmentService } from './case-assignment.service';

describe('CaseAssignmentService', () => {
  let service: CaseAssignmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CaseAssignmentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

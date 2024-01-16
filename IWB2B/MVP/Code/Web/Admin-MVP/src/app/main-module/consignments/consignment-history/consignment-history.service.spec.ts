import { TestBed } from '@angular/core/testing';

import { ConsignmentHistoryService } from './consignment-history.service';

describe('ConsignmentHistoryService', () => {
  let service: ConsignmentHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConsignmentHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

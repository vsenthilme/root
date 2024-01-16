import { TestBed } from '@angular/core/testing';

import { ConsignmentNewService } from './consignment-new.service';

describe('ConsignmentNewService', () => {
  let service: ConsignmentNewService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConsignmentNewService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

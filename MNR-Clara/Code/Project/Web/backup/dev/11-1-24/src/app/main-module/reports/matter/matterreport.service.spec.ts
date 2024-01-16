import { TestBed } from '@angular/core/testing';

import { MatterreportService } from './matterreport.service';

describe('MatterreportService', () => {
  let service: MatterreportService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MatterreportService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

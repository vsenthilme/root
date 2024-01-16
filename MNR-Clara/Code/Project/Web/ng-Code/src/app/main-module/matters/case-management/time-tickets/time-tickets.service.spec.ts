import { TestBed } from '@angular/core/testing';

import { TimeTicketsService } from './time-tickets.service';

describe('TimeTicketsService', () => {
  let service: TimeTicketsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TimeTicketsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

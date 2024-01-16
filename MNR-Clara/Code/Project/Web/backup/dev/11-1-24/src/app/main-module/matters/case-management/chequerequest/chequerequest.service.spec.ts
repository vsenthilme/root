import { TestBed } from '@angular/core/testing';

import { ChequerequestService } from './chequerequest.service';

describe('ChequerequestService', () => {
  let service: ChequerequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChequerequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

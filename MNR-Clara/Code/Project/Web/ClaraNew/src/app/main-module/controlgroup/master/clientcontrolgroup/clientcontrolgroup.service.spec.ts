import { TestBed } from '@angular/core/testing';

import { ClientcontrolgroupService } from './clientcontrolgroup.service';

describe('ClientcontrolgroupService', () => {
  let service: ClientcontrolgroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientcontrolgroupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

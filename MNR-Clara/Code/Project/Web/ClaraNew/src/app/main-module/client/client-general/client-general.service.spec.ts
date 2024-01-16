import { TestBed } from '@angular/core/testing';

import { ClientGeneralService } from './client-general.service';

describe('ClientGeneralService', () => {
  let service: ClientGeneralService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientGeneralService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

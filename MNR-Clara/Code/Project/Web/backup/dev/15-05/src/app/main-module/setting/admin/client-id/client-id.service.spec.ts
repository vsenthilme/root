import { TestBed } from '@angular/core/testing';

import { ClientIdService } from './client-id.service';

describe('ClientIdService', () => {
  let service: ClientIdService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientIdService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

import { TestBed } from '@angular/core/testing';

import { CliententityassignementService } from './cliententityassignement.service';

describe('CliententityassignementService', () => {
  let service: CliententityassignementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CliententityassignementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

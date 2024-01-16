import { TestBed } from '@angular/core/testing';

import { ClientNotesService } from './client-notes.service';

describe('ClientNotesService', () => {
  let service: ClientNotesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientNotesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

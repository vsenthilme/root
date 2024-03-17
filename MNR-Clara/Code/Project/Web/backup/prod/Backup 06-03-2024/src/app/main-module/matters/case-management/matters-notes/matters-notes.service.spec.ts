import { TestBed } from '@angular/core/testing';

import { MattersNotesService } from './matters-notes.service';

describe('MattersNotesService', () => {
  let service: MattersNotesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MattersNotesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

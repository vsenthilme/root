import { TestBed } from '@angular/core/testing';

import { ClientDocumentService } from './client-document.service';

describe('ClientDocumentService', () => {
  let service: ClientDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientDocumentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

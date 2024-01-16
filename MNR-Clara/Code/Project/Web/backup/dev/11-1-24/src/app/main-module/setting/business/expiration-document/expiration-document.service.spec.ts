import { TestBed } from '@angular/core/testing';

import { ExpirationDocumentService } from './expiration-document.service';

describe('ExpirationDocumentService', () => {
  let service: ExpirationDocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExpirationDocumentService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

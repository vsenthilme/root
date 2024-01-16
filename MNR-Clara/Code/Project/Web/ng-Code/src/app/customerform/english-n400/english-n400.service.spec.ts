import { TestBed } from '@angular/core/testing';

import { EnglishN400Service } from './english-n400.service';

describe('EnglishN400Service', () => {
  let service: EnglishN400Service;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EnglishN400Service);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

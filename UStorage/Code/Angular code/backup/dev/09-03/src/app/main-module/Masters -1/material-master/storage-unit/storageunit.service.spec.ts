import { TestBed } from '@angular/core/testing';

import { StorageunitService } from './storageunit.service';

describe('StorageunitService', () => {
  let service: StorageunitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StorageunitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

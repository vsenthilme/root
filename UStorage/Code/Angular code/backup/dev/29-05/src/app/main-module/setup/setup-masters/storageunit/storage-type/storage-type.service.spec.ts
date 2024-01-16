import { TestBed } from '@angular/core/testing';

import { StorageTypeService } from './storage-type.service';

describe('StorageTypeService', () => {
  let service: StorageTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StorageTypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

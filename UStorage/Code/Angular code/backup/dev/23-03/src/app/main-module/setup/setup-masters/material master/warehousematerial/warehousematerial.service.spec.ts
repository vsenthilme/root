import { TestBed } from '@angular/core/testing';

import { WarehousematerialService } from './warehousematerial.service';

describe('WarehousematerialService', () => {
  let service: WarehousematerialService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WarehousematerialService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

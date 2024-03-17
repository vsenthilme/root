import { TestBed } from '@angular/core/testing';

import { WarehhousetypeNewService } from './warehhousetype-new.service';

describe('WarehhousetypeNewService', () => {
  let service: WarehhousetypeNewService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WarehhousetypeNewService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

import { TestBed } from '@angular/core/testing';

import { JntOrdersService } from './jnt-orders.service';

describe('JntOrdersService', () => {
  let service: JntOrdersService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JntOrdersService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

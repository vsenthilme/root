import { TestBed } from '@angular/core/testing';

import { MatterFeesSharingService } from './matter-fees-sharing.service';

describe('MatterFeesSharingService', () => {
  let service: MatterFeesSharingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MatterFeesSharingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

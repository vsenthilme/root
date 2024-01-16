import { TestBed } from '@angular/core/testing';

import { SetupServiceService } from './setup-service.service';

describe('SetupServiceService', () => {
  let service: SetupServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SetupServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

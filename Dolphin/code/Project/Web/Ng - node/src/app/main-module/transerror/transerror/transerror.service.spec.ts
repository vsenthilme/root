import { TestBed } from '@angular/core/testing';

import { TranserrorService } from './transerror.service';

describe('TranserrorService', () => {
  let service: TranserrorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TranserrorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

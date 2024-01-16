import { TestBed } from '@angular/core/testing';

import { InquiresService } from './inquires.service';

describe('InquiresService', () => {
  let service: InquiresService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InquiresService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

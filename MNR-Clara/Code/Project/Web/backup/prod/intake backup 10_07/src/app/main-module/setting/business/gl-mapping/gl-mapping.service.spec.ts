import { TestBed } from '@angular/core/testing';

import { GlMappingService } from './gl-mapping.service';

describe('GlMappingService', () => {
  let service: GlMappingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GlMappingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

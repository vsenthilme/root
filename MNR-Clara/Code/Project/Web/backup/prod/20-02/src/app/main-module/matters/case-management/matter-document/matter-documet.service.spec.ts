import { TestBed } from '@angular/core/testing';

import { MatterDocumetService } from './matter-documet.service';

describe('MatterDocumetService', () => {
  let service: MatterDocumetService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MatterDocumetService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

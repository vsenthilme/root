import { TestBed } from '@angular/core/testing';

import { MatterIntakeService } from './matter-intake.service';

describe('MatterIntakeService', () => {
  let service: MatterIntakeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MatterIntakeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

import { TestBed } from '@angular/core/testing';

import { TaskbaseService } from './taskbase.service';

describe('TaskbaseService', () => {
  let service: TaskbaseService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TaskbaseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

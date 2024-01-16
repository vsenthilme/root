import { TestBed } from '@angular/core/testing';

import { TaskMatterService } from './task-matter.service';

describe('TaskMatterService', () => {
  let service: TaskMatterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TaskMatterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

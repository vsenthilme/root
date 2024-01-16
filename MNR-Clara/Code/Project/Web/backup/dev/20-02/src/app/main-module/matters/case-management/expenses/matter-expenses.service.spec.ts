import { TestBed } from '@angular/core/testing';

import { MatterExpensesService } from './matter-expenses.service';

describe('MatterExpensesService', () => {
  let service: MatterExpensesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MatterExpensesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

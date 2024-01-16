import { TestBed } from '@angular/core/testing';

import { DacaFormService } from './daca-form.service';

describe('DacaFormService', () => {
  let service: DacaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DacaFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

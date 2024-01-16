import { TestBed } from '@angular/core/testing';

import { Form009Service } from './form009.service';

describe('Form009Service', () => {
  let service: Form009Service;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Form009Service);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

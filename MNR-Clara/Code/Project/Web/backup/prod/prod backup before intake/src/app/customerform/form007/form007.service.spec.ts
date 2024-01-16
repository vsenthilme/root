import { TestBed } from '@angular/core/testing';

import { Form007Service } from './form007.service';

describe('Form007Service', () => {
  let service: Form007Service;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Form007Service);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

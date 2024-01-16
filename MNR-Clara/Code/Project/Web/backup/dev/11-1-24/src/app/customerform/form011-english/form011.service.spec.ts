import { TestBed } from '@angular/core/testing';

import { Form011Service } from './form011.service';

describe('Form011Service', () => {
  let service: Form011Service;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Form011Service);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

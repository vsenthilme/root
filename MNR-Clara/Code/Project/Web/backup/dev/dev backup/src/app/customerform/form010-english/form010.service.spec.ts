import { TestBed } from '@angular/core/testing';

import { Form010Service } from './form010.service';

describe('Form010Service', () => {
  let service: Form010Service;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Form010Service);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

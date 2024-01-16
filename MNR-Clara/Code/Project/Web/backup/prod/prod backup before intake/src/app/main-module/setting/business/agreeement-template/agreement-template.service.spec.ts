import { TestBed } from '@angular/core/testing';

import { AgreementTemplateService } from './agreement-template.service';

describe('AgreementTemplateService', () => {
  let service: AgreementTemplateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgreementTemplateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

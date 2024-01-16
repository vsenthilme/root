import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgreementRenewComponent } from './agreement-renew.component';

describe('AgreementRenewComponent', () => {
  let component: AgreementRenewComponent;
  let fixture: ComponentFixture<AgreementRenewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AgreementRenewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgreementRenewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

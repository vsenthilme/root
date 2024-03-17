import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgreementDocumentComponent } from './agreement-document.component';

describe('AgreementDocumentComponent', () => {
  let component: AgreementDocumentComponent;
  let fixture: ComponentFixture<AgreementDocumentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AgreementDocumentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgreementDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

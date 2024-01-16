import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgreementPopupNewComponent } from './agreement-popup-new.component';

describe('AgreementPopupNewComponent', () => {
  let component: AgreementPopupNewComponent;
  let fixture: ComponentFixture<AgreementPopupNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AgreementPopupNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgreementPopupNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

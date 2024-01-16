import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgreementTemplateViewComponent } from './agreement-template-view.component';

describe('AgreementTemplateViewComponent', () => {
  let component: AgreementTemplateViewComponent;
  let fixture: ComponentFixture<AgreementTemplateViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AgreementTemplateViewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgreementTemplateViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

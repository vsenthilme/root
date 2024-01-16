import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgreementtemplateDisplayComponent } from './agreementtemplate-display.component';

describe('AgreementtemplateDisplayComponent', () => {
  let component: AgreementtemplateDisplayComponent;
  let fixture: ComponentFixture<AgreementtemplateDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AgreementtemplateDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AgreementtemplateDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

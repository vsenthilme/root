import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntakePopupComponent } from './intake-popup.component';

describe('IntakePopupComponent', () => {
  let component: IntakePopupComponent;
  let fixture: ComponentFixture<IntakePopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntakePopupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntakePopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

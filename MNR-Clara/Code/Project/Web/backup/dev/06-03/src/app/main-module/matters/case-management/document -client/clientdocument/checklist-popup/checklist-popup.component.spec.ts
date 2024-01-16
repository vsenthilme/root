import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChecklistPopupComponent } from './checklist-popup.component';

describe('ChecklistPopupComponent', () => {
  let component: ChecklistPopupComponent;
  let fixture: ComponentFixture<ChecklistPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChecklistPopupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChecklistPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

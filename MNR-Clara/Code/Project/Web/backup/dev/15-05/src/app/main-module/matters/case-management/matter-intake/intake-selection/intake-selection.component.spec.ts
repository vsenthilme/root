import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntakeSelectionComponent } from './intake-selection.component';

describe('IntakeSelectionComponent', () => {
  let component: IntakeSelectionComponent;
  let fixture: ComponentFixture<IntakeSelectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntakeSelectionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntakeSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

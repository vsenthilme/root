import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntakeSnapMainComponent } from './intake-snap-main.component';

describe('IntakeSnapMainComponent', () => {
  let component: IntakeSnapMainComponent;
  let fixture: ComponentFixture<IntakeSnapMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntakeSnapMainComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntakeSnapMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

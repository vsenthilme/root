import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeadlineCalculatorComponent } from './deadline-calculator.component';

describe('DeadlineCalculatorComponent', () => {
  let component: DeadlineCalculatorComponent;
  let fixture: ComponentFixture<DeadlineCalculatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeadlineCalculatorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeadlineCalculatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

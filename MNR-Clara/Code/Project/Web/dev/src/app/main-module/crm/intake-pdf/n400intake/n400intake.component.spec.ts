import { ComponentFixture, TestBed } from '@angular/core/testing';

import { N400intakeComponent } from './n400intake.component';

describe('N400intakeComponent', () => {
  let component: N400intakeComponent;
  let fixture: ComponentFixture<N400intakeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ N400intakeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(N400intakeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

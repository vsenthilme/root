import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntakeIdComponent } from './intake-id.component';

describe('IntakeIdComponent', () => {
  let component: IntakeIdComponent;
  let fixture: ComponentFixture<IntakeIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntakeIdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntakeIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

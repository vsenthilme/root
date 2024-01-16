import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskcodeDisplayComponent } from './taskcode-display.component';

describe('TaskcodeDisplayComponent', () => {
  let component: TaskcodeDisplayComponent;
  let fixture: ComponentFixture<TaskcodeDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TaskcodeDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskcodeDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

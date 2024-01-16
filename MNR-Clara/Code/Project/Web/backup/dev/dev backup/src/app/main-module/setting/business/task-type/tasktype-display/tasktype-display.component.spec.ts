import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TasktypeDisplayComponent } from './tasktype-display.component';

describe('TasktypeDisplayComponent', () => {
  let component: TasktypeDisplayComponent;
  let fixture: ComponentFixture<TasktypeDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TasktypeDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TasktypeDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

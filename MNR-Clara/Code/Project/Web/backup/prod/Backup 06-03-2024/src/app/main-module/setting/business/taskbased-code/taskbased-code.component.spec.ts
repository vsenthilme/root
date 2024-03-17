import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskbasedCodeComponent } from './taskbased-code.component';

describe('TaskbasedCodeComponent', () => {
  let component: TaskbasedCodeComponent;
  let fixture: ComponentFixture<TaskbasedCodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TaskbasedCodeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TaskbasedCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

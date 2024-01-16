import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClassDisplayComponent } from './class-display.component';

describe('ClassDisplayComponent', () => {
  let component: ClassDisplayComponent;
  let fixture: ComponentFixture<ClassDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClassDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClassDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

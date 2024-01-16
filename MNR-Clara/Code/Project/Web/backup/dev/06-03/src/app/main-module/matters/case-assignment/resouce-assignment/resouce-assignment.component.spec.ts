import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResouceAssignmentComponent } from './resouce-assignment.component';

describe('ResouceAssignmentComponent', () => {
  let component: ResouceAssignmentComponent;
  let fixture: ComponentFixture<ResouceAssignmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ResouceAssignmentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ResouceAssignmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

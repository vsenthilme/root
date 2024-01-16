import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaseassignmentListComponent } from './caseassignment-list.component';

describe('CaseassignmentListComponent', () => {
  let component: CaseassignmentListComponent;
  let fixture: ComponentFixture<CaseassignmentListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CaseassignmentListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CaseassignmentListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

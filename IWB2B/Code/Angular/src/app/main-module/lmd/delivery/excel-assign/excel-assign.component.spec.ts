import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExcelAssignComponent } from './excel-assign.component';

describe('ExcelAssignComponent', () => {
  let component: ExcelAssignComponent;
  let fixture: ComponentFixture<ExcelAssignComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExcelAssignComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExcelAssignComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

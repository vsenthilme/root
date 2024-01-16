import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeEfficiencyComponent } from './employee-efficiency.component';

describe('EmployeeEfficiencyComponent', () => {
  let component: EmployeeEfficiencyComponent;
  let fixture: ComponentFixture<EmployeeEfficiencyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmployeeEfficiencyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmployeeEfficiencyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

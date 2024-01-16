import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmploymentHistoryForm007Component } from './employment-history-form007.component';

describe('EmploymentHistoryForm007Component', () => {
  let component: EmploymentHistoryForm007Component;
  let fixture: ComponentFixture<EmploymentHistoryForm007Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmploymentHistoryForm007Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EmploymentHistoryForm007Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

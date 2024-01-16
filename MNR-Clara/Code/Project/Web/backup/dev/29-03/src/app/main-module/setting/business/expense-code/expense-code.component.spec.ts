import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpenseCodeComponent } from './expense-code.component';

describe('ExpenseCodeComponent', () => {
  let component: ExpenseCodeComponent;
  let fixture: ComponentFixture<ExpenseCodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExpenseCodeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpenseCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

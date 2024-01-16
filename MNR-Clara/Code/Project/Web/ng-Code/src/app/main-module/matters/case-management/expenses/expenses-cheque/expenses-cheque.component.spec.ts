import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpensesChequeComponent } from './expenses-cheque.component';

describe('ExpensesChequeComponent', () => {
  let component: ExpensesChequeComponent;
  let fixture: ComponentFixture<ExpensesChequeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExpensesChequeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpensesChequeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

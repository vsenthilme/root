import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpensesNewComponent } from './expenses-new.component';

describe('ExpensesNewComponent', () => {
  let component: ExpensesNewComponent;
  let fixture: ComponentFixture<ExpensesNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExpensesNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpensesNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

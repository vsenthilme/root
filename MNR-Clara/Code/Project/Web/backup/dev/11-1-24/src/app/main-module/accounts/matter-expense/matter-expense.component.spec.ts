import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatterExpenseComponent } from './matter-expense.component';

describe('MatterExpenseComponent', () => {
  let component: MatterExpenseComponent;
  let fixture: ComponentFixture<MatterExpenseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatterExpenseComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatterExpenseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

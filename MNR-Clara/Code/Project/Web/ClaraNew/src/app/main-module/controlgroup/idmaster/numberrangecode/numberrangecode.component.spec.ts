import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NumberrangecodeComponent } from './numberrangecode.component';

describe('NumberrangecodeComponent', () => {
  let component: NumberrangecodeComponent;
  let fixture: ComponentFixture<NumberrangecodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NumberrangecodeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NumberrangecodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

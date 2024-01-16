import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NumberrangeDisplayComponent } from './numberrange-display.component';

describe('NumberrangeDisplayComponent', () => {
  let component: NumberrangeDisplayComponent;
  let fixture: ComponentFixture<NumberrangeDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NumberrangeDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NumberrangeDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

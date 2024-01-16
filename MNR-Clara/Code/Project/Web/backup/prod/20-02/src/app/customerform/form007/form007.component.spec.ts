import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Form007Component } from './form007.component';

describe('Form007Component', () => {
  let component: Form007Component;
  let fixture: ComponentFixture<Form007Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Form007Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Form007Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

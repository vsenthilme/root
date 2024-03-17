import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImmigirationFrom007Component } from './immigiration-from007.component';

describe('ImmigirationFrom007Component', () => {
  let component: ImmigirationFrom007Component;
  let fixture: ComponentFixture<ImmigirationFrom007Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImmigirationFrom007Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImmigirationFrom007Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

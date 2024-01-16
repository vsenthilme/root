import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Spanish003Component } from './spanish003.component';

describe('Spanish003Component', () => {
  let component: Spanish003Component;
  let fixture: ComponentFixture<Spanish003Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Spanish003Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Spanish003Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

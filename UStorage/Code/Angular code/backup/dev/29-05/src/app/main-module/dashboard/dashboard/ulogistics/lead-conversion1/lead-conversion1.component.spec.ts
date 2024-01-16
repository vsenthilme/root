import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeadConversion1Component } from './lead-conversion1.component';

describe('LeadConversion1Component', () => {
  let component: LeadConversion1Component;
  let fixture: ComponentFixture<LeadConversion1Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LeadConversion1Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LeadConversion1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

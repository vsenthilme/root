import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeadInputComponent } from './lead-input.component';

describe('LeadInputComponent', () => {
  let component: LeadInputComponent;
  let fixture: ComponentFixture<LeadInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LeadInputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LeadInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeadOutputComponent } from './lead-output.component';

describe('LeadOutputComponent', () => {
  let component: LeadOutputComponent;
  let fixture: ComponentFixture<LeadOutputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LeadOutputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LeadOutputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

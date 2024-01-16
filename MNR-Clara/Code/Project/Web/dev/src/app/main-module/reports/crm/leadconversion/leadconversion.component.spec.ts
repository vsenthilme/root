import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeadconversionComponent } from './leadconversion.component';

describe('LeadconversionComponent', () => {
  let component: LeadconversionComponent;
  let fixture: ComponentFixture<LeadconversionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LeadconversionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LeadconversionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

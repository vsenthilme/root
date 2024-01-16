import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeReportComponent } from './le-report.component';

describe('LeReportComponent', () => {
  let component: LeReportComponent;
  let fixture: ComponentFixture<LeReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LeReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LeReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

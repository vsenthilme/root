import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcademyReportComponent } from './academy-report.component';

describe('AcademyReportComponent', () => {
  let component: AcademyReportComponent;
  let fixture: ComponentFixture<AcademyReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AcademyReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AcademyReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

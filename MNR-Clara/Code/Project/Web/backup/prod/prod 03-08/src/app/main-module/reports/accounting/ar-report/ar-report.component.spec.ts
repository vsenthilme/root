import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArReportComponent } from './ar-report.component';

describe('ArReportComponent', () => {
  let component: ArReportComponent;
  let fixture: ComponentFixture<ArReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

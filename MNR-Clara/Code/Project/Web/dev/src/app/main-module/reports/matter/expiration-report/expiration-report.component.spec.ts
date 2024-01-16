import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpirationReportComponent } from './expiration-report.component';

describe('ExpirationReportComponent', () => {
  let component: ExpirationReportComponent;
  let fixture: ComponentFixture<ExpirationReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExpirationReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpirationReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

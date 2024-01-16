import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoutiqaatReportComponent } from './boutiqaat-report.component';

describe('BoutiqaatReportComponent', () => {
  let component: BoutiqaatReportComponent;
  let fixture: ComponentFixture<BoutiqaatReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BoutiqaatReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BoutiqaatReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

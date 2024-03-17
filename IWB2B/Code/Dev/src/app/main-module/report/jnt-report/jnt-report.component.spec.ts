import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JntReportComponent } from './jnt-report.component';

describe('JntReportComponent', () => {
  let component: JntReportComponent;
  let fixture: ComponentFixture<JntReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JntReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(JntReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

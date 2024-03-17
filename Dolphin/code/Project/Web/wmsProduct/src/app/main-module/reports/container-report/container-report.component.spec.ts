import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerReportComponent } from './container-report.component';

describe('ContainerReportComponent', () => {
  let component: ContainerReportComponent;
  let fixture: ComponentFixture<ContainerReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ContainerReportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

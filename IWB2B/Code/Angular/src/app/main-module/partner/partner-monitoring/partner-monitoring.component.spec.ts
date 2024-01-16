import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartnerMonitoringComponent } from './partner-monitoring.component';

describe('PartnerMonitoringComponent', () => {
  let component: PartnerMonitoringComponent;
  let fixture: ComponentFixture<PartnerMonitoringComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartnerMonitoringComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PartnerMonitoringComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

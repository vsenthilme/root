import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitoringControlComponent } from './monitoring-control.component';

describe('MonitoringControlComponent', () => {
  let component: MonitoringControlComponent;
  let fixture: ComponentFixture<MonitoringControlComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MonitoringControlComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MonitoringControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

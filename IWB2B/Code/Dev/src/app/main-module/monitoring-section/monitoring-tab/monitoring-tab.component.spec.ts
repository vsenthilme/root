import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitoringTabComponent } from './monitoring-tab.component';

describe('MonitoringTabComponent', () => {
  let component: MonitoringTabComponent;
  let fixture: ComponentFixture<MonitoringTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MonitoringTabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MonitoringTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

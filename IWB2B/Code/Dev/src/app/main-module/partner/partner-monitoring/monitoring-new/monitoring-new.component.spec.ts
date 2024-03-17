import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitoringNewComponent } from './monitoring-new.component';

describe('MonitoringNewComponent', () => {
  let component: MonitoringNewComponent;
  let fixture: ComponentFixture<MonitoringNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MonitoringNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MonitoringNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeticketreportComponent } from './timeticketreport.component';

describe('TimeticketreportComponent', () => {
  let component: TimeticketreportComponent;
  let fixture: ComponentFixture<TimeticketreportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TimeticketreportComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TimeticketreportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

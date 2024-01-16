import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeTicketsComponent } from './time-tickets.component';

describe('TimeTicketsComponent', () => {
  let component: TimeTicketsComponent;
  let fixture: ComponentFixture<TimeTicketsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TimeTicketsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TimeTicketsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

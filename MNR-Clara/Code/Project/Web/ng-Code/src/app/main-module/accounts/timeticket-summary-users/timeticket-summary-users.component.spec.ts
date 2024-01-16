import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeticketSummaryUsersComponent } from './timeticket-summary-users.component';

describe('TimeticketSummaryUsersComponent', () => {
  let component: TimeticketSummaryUsersComponent;
  let fixture: ComponentFixture<TimeticketSummaryUsersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TimeticketSummaryUsersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TimeticketSummaryUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

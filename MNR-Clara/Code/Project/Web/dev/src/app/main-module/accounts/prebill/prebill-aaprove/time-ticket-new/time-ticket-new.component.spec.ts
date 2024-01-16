import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeTicketNewComponent } from './time-ticket-new.component';

describe('TimeTicketNewComponent', () => {
  let component: TimeTicketNewComponent;
  let fixture: ComponentFixture<TimeTicketNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TimeTicketNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TimeTicketNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

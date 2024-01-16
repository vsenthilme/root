import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeNewComponent } from './time-new.component';

describe('TimeNewComponent', () => {
  let component: TimeNewComponent;
  let fixture: ComponentFixture<TimeNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TimeNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TimeNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

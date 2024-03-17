import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimekeeperDisplayComponent } from './timekeeper-display.component';

describe('TimekeeperDisplayComponent', () => {
  let component: TimekeeperDisplayComponent;
  let fixture: ComponentFixture<TimekeeperDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TimekeeperDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TimekeeperDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

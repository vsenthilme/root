import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivitycodeDisplayComponent } from './activitycode-display.component';

describe('ActivitycodeDisplayComponent', () => {
  let component: ActivitycodeDisplayComponent;
  let fixture: ComponentFixture<ActivitycodeDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActivitycodeDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivitycodeDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

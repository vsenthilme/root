import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivityCodeComponent } from './activity-code.component';

describe('ActivityCodeComponent', () => {
  let component: ActivityCodeComponent;
  let fixture: ComponentFixture<ActivityCodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActivityCodeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ActivityCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

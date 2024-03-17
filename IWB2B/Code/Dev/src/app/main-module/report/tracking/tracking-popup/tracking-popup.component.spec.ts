import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrackingPopupComponent } from './tracking-popup.component';

describe('TrackingPopupComponent', () => {
  let component: TrackingPopupComponent;
  let fixture: ComponentFixture<TrackingPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TrackingPopupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TrackingPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

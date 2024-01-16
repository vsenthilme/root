import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TripsNewComponent } from './trips-new.component';

describe('TripsNewComponent', () => {
  let component: TripsNewComponent;
  let fixture: ComponentFixture<TripsNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TripsNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TripsNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

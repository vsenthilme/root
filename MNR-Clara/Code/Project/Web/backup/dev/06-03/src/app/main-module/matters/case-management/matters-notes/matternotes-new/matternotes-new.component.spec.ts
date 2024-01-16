import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatternotesNewComponent } from './matternotes-new.component';

describe('MatternotesNewComponent', () => {
  let component: MatternotesNewComponent;
  let fixture: ComponentFixture<MatternotesNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatternotesNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatternotesNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

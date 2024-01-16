import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LandENewComponent } from './land-e-new.component';

describe('LandENewComponent', () => {
  let component: LandENewComponent;
  let fixture: ComponentFixture<LandENewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LandENewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LandENewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

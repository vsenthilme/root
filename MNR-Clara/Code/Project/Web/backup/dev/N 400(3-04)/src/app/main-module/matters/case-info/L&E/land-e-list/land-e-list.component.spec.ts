import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LandEListComponent } from './land-e-list.component';

describe('LandEListComponent', () => {
  let component: LandEListComponent;
  let fixture: ComponentFixture<LandEListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LandEListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LandEListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

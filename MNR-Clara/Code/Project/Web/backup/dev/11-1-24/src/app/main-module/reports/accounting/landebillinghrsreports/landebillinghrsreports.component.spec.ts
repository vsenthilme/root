import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LandebillinghrsreportsComponent } from './landebillinghrsreports.component';

describe('LandebillinghrsreportsComponent', () => {
  let component: LandebillinghrsreportsComponent;
  let fixture: ComponentFixture<LandebillinghrsreportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LandebillinghrsreportsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LandebillinghrsreportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

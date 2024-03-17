import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SoorderlinesComponent } from './soorderlines.component';

describe('SoorderlinesComponent', () => {
  let component: SoorderlinesComponent;
  let fixture: ComponentFixture<SoorderlinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SoorderlinesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SoorderlinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LandeMatterComponent } from './lande-matter.component';

describe('LandeMatterComponent', () => {
  let component: LandeMatterComponent;
  let fixture: ComponentFixture<LandeMatterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LandeMatterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LandeMatterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

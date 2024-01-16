import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FillrateComponent } from './fillrate.component';

describe('FillrateComponent', () => {
  let component: FillrateComponent;
  let fixture: ComponentFixture<FillrateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FillrateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FillrateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PickProductivityComponent } from './pick-productivity.component';

describe('PickProductivityComponent', () => {
  let component: PickProductivityComponent;
  let fixture: ComponentFixture<PickProductivityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PickProductivityComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PickProductivityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

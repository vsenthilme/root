import { ComponentFixture, TestBed } from '@angular/core/testing';

import { B2borderlinesComponent } from './b2borderlines.component';

describe('B2borderlinesComponent', () => {
  let component: B2borderlinesComponent;
  let fixture: ComponentFixture<B2borderlinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ B2borderlinesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(B2borderlinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

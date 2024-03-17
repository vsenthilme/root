import { ComponentFixture, TestBed } from '@angular/core/testing';

import { B2borderComponent } from './b2border.component';

describe('B2borderComponent', () => {
  let component: B2borderComponent;
  let fixture: ComponentFixture<B2borderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ B2borderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(B2borderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

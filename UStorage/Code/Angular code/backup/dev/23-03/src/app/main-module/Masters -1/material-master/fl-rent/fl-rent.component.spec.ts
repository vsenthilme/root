import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FlRentComponent } from './fl-rent.component';

describe('FlRentComponent', () => {
  let component: FlRentComponent;
  let fixture: ComponentFixture<FlRentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FlRentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FlRentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

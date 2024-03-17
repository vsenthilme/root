import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChartDisplayComponent } from './chart-display.component';

describe('ChartDisplayComponent', () => {
  let component: ChartDisplayComponent;
  let fixture: ComponentFixture<ChartDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChartDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChartDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

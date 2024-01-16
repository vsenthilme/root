import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NumberingSeriesComponent } from './numbering-series.component';

describe('NumberingSeriesComponent', () => {
  let component: NumberingSeriesComponent;
  let fixture: ComponentFixture<NumberingSeriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NumberingSeriesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NumberingSeriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

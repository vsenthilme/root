import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoryResidenceForm007Component } from './history-residence-form007.component';

describe('HistoryResidenceForm007Component', () => {
  let component: HistoryResidenceForm007Component;
  let fixture: ComponentFixture<HistoryResidenceForm007Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HistoryResidenceForm007Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoryResidenceForm007Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

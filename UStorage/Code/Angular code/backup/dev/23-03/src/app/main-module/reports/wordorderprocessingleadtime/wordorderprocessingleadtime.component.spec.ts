import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WordorderprocessingleadtimeComponent } from './wordorderprocessingleadtime.component';

describe('WordorderprocessingleadtimeComponent', () => {
  let component: WordorderprocessingleadtimeComponent;
  let fixture: ComponentFixture<WordorderprocessingleadtimeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WordorderprocessingleadtimeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WordorderprocessingleadtimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

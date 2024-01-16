import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HandlingchargesComponent } from './handlingcharges.component';

describe('HandlingchargesComponent', () => {
  let component: HandlingchargesComponent;
  let fixture: ComponentFixture<HandlingchargesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HandlingchargesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HandlingchargesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

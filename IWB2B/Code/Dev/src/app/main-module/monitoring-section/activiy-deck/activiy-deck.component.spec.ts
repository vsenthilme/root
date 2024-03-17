import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiviyDeckComponent } from './activiy-deck.component';

describe('ActiviyDeckComponent', () => {
  let component: ActiviyDeckComponent;
  let fixture: ComponentFixture<ActiviyDeckComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActiviyDeckComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ActiviyDeckComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ThreepltabComponent } from './threepltab.component';

describe('ThreepltabComponent', () => {
  let component: ThreepltabComponent;
  let fixture: ComponentFixture<ThreepltabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ThreepltabComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ThreepltabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

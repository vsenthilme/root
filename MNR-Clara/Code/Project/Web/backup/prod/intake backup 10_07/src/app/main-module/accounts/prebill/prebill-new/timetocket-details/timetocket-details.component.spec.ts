import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimetocketDetailsComponent } from './timetocket-details.component';

describe('TimetocketDetailsComponent', () => {
  let component: TimetocketDetailsComponent;
  let fixture: ComponentFixture<TimetocketDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TimetocketDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TimetocketDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

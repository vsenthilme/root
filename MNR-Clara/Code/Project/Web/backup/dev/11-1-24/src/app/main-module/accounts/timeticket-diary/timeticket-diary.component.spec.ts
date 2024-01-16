import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimeticketDiaryComponent } from './timeticket-diary.component';

describe('TimeticketDiaryComponent', () => {
  let component: TimeticketDiaryComponent;
  let fixture: ComponentFixture<TimeticketDiaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TimeticketDiaryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TimeticketDiaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

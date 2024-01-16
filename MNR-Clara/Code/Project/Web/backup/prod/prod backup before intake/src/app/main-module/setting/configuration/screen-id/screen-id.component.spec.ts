import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScreenIdComponent } from './screen-id.component';

describe('ScreenIdComponent', () => {
  let component: ScreenIdComponent;
  let fixture: ComponentFixture<ScreenIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScreenIdComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ScreenIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

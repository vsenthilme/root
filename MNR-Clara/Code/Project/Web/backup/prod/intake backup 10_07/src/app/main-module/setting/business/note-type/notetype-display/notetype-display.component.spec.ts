import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotetypeDisplayComponent } from './notetype-display.component';

describe('NotetypeDisplayComponent', () => {
  let component: NotetypeDisplayComponent;
  let fixture: ComponentFixture<NotetypeDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotetypeDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NotetypeDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

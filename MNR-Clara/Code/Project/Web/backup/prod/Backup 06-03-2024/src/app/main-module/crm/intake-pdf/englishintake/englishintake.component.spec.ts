import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnglishintakeComponent } from './englishintake.component';

describe('EnglishintakeComponent', () => {
  let component: EnglishintakeComponent;
  let fixture: ComponentFixture<EnglishintakeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EnglishintakeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EnglishintakeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

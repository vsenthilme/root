import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnglishN400Component } from './english-n400.component';

describe('EnglishN400Component', () => {
  let component: EnglishN400Component;
  let fixture: ComponentFixture<EnglishN400Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EnglishN400Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EnglishN400Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Form011EnglishComponent } from './form011-english.component';

describe('Form011EnglishComponent', () => {
  let component: Form011EnglishComponent;
  let fixture: ComponentFixture<Form011EnglishComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Form011EnglishComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Form011EnglishComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

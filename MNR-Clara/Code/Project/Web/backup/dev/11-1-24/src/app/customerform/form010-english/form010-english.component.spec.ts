import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Form010EnglishComponent } from './form010-english.component';

describe('Form010EnglishComponent', () => {
  let component: Form010EnglishComponent;
  let fixture: ComponentFixture<Form010EnglishComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Form010EnglishComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Form010EnglishComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

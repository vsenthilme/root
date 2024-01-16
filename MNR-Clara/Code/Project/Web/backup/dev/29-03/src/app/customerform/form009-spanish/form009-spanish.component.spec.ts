import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Form009SpanishComponent } from './form009-spanish.component';

describe('Form009SpanishComponent', () => {
  let component: Form009SpanishComponent;
  let fixture: ComponentFixture<Form009SpanishComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Form009SpanishComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Form009SpanishComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

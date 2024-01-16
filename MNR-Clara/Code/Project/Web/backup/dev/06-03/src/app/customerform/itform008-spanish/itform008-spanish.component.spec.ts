import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Itform008SpanishComponent } from './itform008-spanish.component';

describe('Itform008SpanishComponent', () => {
  let component: Itform008SpanishComponent;
  let fixture: ComponentFixture<Itform008SpanishComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ Itform008SpanishComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(Itform008SpanishComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

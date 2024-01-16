import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NoteTypeComponent } from './note-type.component';

describe('NoteTypeComponent', () => {
  let component: NoteTypeComponent;
  let fixture: ComponentFixture<NoteTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NoteTypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NoteTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

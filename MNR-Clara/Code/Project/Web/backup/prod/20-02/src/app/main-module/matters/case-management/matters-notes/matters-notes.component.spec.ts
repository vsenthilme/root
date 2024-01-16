import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MattersNotesComponent } from './matters-notes.component';

describe('MattersNotesComponent', () => {
  let component: MattersNotesComponent;
  let fixture: ComponentFixture<MattersNotesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MattersNotesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MattersNotesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

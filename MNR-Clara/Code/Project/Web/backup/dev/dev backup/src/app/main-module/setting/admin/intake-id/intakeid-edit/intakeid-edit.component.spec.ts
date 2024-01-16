import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntakeidEditComponent } from './intakeid-edit.component';

describe('IntakeidEditComponent', () => {
  let component: IntakeidEditComponent;
  let fixture: ComponentFixture<IntakeidEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IntakeidEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntakeidEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

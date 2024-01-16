import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProspectiveInputComponent } from './prospective-input.component';

describe('ProspectiveInputComponent', () => {
  let component: ProspectiveInputComponent;
  let fixture: ComponentFixture<ProspectiveInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProspectiveInputComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProspectiveInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

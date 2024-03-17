import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpenceDetailsComponent } from './expence-details.component';

describe('ExpenceDetailsComponent', () => {
  let component: ExpenceDetailsComponent;
  let fixture: ComponentFixture<ExpenceDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExpenceDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpenceDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

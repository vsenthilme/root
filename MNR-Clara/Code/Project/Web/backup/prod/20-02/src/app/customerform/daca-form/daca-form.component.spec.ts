import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DacaFormComponent } from './daca-form.component';

describe('DacaFormComponent', () => {
  let component: DacaFormComponent;
  let fixture: ComponentFixture<DacaFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DacaFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DacaFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

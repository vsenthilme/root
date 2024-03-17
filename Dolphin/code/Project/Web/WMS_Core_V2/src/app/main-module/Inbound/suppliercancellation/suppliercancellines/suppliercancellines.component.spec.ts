import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuppliercancellinesComponent } from './suppliercancellines.component';

describe('SuppliercancellinesComponent', () => {
  let component: SuppliercancellinesComponent;
  let fixture: ComponentFixture<SuppliercancellinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SuppliercancellinesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SuppliercancellinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

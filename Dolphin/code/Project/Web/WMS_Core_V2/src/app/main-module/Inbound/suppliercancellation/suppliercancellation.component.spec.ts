import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuppliercancellationComponent } from './suppliercancellation.component';

describe('SuppliercancellationComponent', () => {
  let component: SuppliercancellationComponent;
  let fixture: ComponentFixture<SuppliercancellationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SuppliercancellationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SuppliercancellationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

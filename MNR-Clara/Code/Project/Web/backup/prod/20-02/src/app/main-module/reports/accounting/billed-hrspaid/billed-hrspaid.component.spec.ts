import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BilledHrspaidComponent } from './billed-hrspaid.component';

describe('BilledHrspaidComponent', () => {
  let component: BilledHrspaidComponent;
  let fixture: ComponentFixture<BilledHrspaidComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BilledHrspaidComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BilledHrspaidComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

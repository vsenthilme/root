import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BilledUnbilledComponent } from './billed-unbilled.component';

describe('BilledUnbilledComponent', () => {
  let component: BilledUnbilledComponent;
  let fixture: ComponentFixture<BilledUnbilledComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BilledUnbilledComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BilledUnbilledComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

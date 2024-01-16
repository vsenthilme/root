import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartnerbillingComponent } from './partnerbilling.component';

describe('PartnerbillingComponent', () => {
  let component: PartnerbillingComponent;
  let fixture: ComponentFixture<PartnerbillingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartnerbillingComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PartnerbillingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

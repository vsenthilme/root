import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImmigrationPaymentComponent } from './immigration-payment.component';

describe('ImmigrationPaymentComponent', () => {
  let component: ImmigrationPaymentComponent;
  let fixture: ComponentFixture<ImmigrationPaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImmigrationPaymentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImmigrationPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

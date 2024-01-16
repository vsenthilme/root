import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerformReceivedComponent } from './customerform-received.component';

describe('CustomerformReceivedComponent', () => {
  let component: CustomerformReceivedComponent;
  let fixture: ComponentFixture<CustomerformReceivedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CustomerformReceivedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerformReceivedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

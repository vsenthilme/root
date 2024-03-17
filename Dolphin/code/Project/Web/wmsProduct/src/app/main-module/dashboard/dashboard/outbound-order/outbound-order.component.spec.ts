import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OutboundOrderComponent } from './outbound-order.component';

describe('OutboundOrderComponent', () => {
  let component: OutboundOrderComponent;
  let fixture: ComponentFixture<OutboundOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OutboundOrderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OutboundOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

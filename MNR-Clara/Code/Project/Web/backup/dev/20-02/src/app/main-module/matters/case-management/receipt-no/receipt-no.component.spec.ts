import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceiptNoComponent } from './receipt-no.component';

describe('ReceiptNoComponent', () => {
  let component: ReceiptNoComponent;
  let fixture: ComponentFixture<ReceiptNoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReceiptNoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReceiptNoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceiptNewComponent } from './receipt-new.component';

describe('ReceiptNewComponent', () => {
  let component: ReceiptNewComponent;
  let fixture: ComponentFixture<ReceiptNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReceiptNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReceiptNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

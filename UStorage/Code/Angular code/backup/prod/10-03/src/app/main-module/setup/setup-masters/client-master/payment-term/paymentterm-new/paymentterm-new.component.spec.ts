import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymenttermNewComponent } from './paymentterm-new.component';

describe('PaymenttermNewComponent', () => {
  let component: PaymenttermNewComponent;
  let fixture: ComponentFixture<PaymenttermNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PaymenttermNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PaymenttermNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuotationPdfComponent } from './quotation-pdf.component';

describe('QuotationPdfComponent', () => {
  let component: QuotationPdfComponent;
  let fixture: ComponentFixture<QuotationPdfComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QuotationPdfComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QuotationPdfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

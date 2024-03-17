import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SavedInvoiceComponent } from './saved-invoice.component';

describe('SavedInvoiceComponent', () => {
  let component: SavedInvoiceComponent;
  let fixture: ComponentFixture<SavedInvoiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SavedInvoiceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SavedInvoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

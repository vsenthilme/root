import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuotationsNewComponent } from './quotations-new.component';

describe('QuotationsNewComponent', () => {
  let component: QuotationsNewComponent;
  let fixture: ComponentFixture<QuotationsNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QuotationsNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(QuotationsNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

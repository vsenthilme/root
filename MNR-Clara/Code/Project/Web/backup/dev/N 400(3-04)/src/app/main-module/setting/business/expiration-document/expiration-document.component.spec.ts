import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpirationDocumentComponent } from './expiration-document.component';

describe('ExpirationDocumentComponent', () => {
  let component: ExpirationDocumentComponent;
  let fixture: ComponentFixture<ExpirationDocumentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExpirationDocumentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpirationDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

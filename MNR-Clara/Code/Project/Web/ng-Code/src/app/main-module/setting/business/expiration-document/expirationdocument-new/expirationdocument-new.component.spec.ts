import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpirationdocumentNewComponent } from './expirationdocument-new.component';

describe('ExpirationdocumentNewComponent', () => {
  let component: ExpirationdocumentNewComponent;
  let fixture: ComponentFixture<ExpirationdocumentNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ExpirationdocumentNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ExpirationdocumentNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

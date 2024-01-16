import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentNewComponent } from './document-new.component';

describe('DocumentNewComponent', () => {
  let component: DocumentNewComponent;
  let fixture: ComponentFixture<DocumentNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DocumentNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

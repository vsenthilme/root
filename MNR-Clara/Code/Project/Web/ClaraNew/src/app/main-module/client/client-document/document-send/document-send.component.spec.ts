import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentSendComponent } from './document-send.component';

describe('DocumentSendComponent', () => {
  let component: DocumentSendComponent;
  let fixture: ComponentFixture<DocumentSendComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DocumentSendComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentSendComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

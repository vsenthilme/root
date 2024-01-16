import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentClientportalComponent } from './document-clientportal.component';

describe('DocumentClientportalComponent', () => {
  let component: DocumentClientportalComponent;
  let fixture: ComponentFixture<DocumentClientportalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DocumentClientportalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentClientportalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

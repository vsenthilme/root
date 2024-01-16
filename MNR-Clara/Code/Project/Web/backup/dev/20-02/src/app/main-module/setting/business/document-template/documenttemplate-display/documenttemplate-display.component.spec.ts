import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumenttemplateDisplayComponent } from './documenttemplate-display.component';

describe('DocumenttemplateDisplayComponent', () => {
  let component: DocumenttemplateDisplayComponent;
  let fixture: ComponentFixture<DocumenttemplateDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DocumenttemplateDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumenttemplateDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

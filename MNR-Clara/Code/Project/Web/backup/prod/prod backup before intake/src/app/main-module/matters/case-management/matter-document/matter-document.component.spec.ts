import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatterDocumentComponent } from './matter-document.component';

describe('MatterDocumentComponent', () => {
  let component: MatterDocumentComponent;
  let fixture: ComponentFixture<MatterDocumentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatterDocumentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatterDocumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DocumentsfromMRComponent } from './documentsfrom-mr.component';

describe('DocumentsfromMRComponent', () => {
  let component: DocumentsfromMRComponent;
  let fixture: ComponentFixture<DocumentsfromMRComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DocumentsfromMRComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DocumentsfromMRComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadCancelComponent } from './upload-cancel.component';

describe('UploadCancelComponent', () => {
  let component: UploadCancelComponent;
  let fixture: ComponentFixture<UploadCancelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UploadCancelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadCancelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

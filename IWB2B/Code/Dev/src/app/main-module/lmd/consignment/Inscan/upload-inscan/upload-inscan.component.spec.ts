import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadInscanComponent } from './upload-inscan.component';

describe('UploadInscanComponent', () => {
  let component: UploadInscanComponent;
  let fixture: ComponentFixture<UploadInscanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UploadInscanComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadInscanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

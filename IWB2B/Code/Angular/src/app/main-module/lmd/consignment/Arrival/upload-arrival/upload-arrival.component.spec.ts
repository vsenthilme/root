import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadArrivalComponent } from './upload-arrival.component';

describe('UploadArrivalComponent', () => {
  let component: UploadArrivalComponent;
  let fixture: ComponentFixture<UploadArrivalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UploadArrivalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadArrivalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

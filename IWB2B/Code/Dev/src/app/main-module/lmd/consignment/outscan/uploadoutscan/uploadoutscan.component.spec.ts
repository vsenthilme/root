import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadoutscanComponent } from './uploadoutscan.component';

describe('UploadoutscanComponent', () => {
  let component: UploadoutscanComponent;
  let fixture: ComponentFixture<UploadoutscanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UploadoutscanComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadoutscanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UploadmatterComponent } from './uploadmatter.component';

describe('UploadmatterComponent', () => {
  let component: UploadmatterComponent;
  let fixture: ComponentFixture<UploadmatterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UploadmatterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UploadmatterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

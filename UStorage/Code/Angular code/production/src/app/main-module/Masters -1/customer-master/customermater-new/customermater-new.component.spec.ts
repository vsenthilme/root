import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomermaterNewComponent } from './customermater-new.component';

describe('CustomermaterNewComponent', () => {
  let component: CustomermaterNewComponent;
  let fixture: ComponentFixture<CustomermaterNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CustomermaterNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomermaterNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

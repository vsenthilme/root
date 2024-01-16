import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemTypeNewComponent } from './system-type-new.component';

describe('SystemTypeNewComponent', () => {
  let component: SystemTypeNewComponent;
  let fixture: ComponentFixture<SystemTypeNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemTypeNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemTypeNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

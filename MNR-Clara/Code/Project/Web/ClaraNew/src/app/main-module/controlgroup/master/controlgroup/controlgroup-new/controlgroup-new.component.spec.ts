import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ControlgroupNewComponent } from './controlgroup-new.component';

describe('ControlgroupNewComponent', () => {
  let component: ControlgroupNewComponent;
  let fixture: ComponentFixture<ControlgroupNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ControlgroupNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ControlgroupNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubsmanagementNewComponent } from './subsmanagement-new.component';

describe('SubsmanagementNewComponent', () => {
  let component: SubsmanagementNewComponent;
  let fixture: ComponentFixture<SubsmanagementNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubsmanagementNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubsmanagementNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

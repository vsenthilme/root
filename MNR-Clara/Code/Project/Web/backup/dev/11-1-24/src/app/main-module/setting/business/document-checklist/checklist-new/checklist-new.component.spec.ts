import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChecklistNewComponent } from './checklist-new.component';

describe('ChecklistNewComponent', () => {
  let component: ChecklistNewComponent;
  let fixture: ComponentFixture<ChecklistNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChecklistNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChecklistNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

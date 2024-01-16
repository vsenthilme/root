import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkorderNewComponent } from './workorder-new.component';

describe('WorkorderNewComponent', () => {
  let component: WorkorderNewComponent;
  let fixture: ComponentFixture<WorkorderNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WorkorderNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkorderNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

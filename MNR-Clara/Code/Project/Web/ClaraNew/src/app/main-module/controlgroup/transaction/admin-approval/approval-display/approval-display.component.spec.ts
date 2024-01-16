import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalDisplayComponent } from './approval-display.component';

describe('ApprovalDisplayComponent', () => {
  let component: ApprovalDisplayComponent;
  let fixture: ComponentFixture<ApprovalDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ApprovalDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

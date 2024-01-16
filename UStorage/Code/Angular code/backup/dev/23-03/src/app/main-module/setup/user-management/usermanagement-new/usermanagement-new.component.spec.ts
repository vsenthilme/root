import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsermanagementNewComponent } from './usermanagement-new.component';

describe('UsermanagementNewComponent', () => {
  let component: UsermanagementNewComponent;
  let fixture: ComponentFixture<UsermanagementNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UsermanagementNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UsermanagementNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

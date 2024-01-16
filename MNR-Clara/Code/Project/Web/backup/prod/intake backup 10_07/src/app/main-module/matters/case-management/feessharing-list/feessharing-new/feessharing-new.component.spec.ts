import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeessharingNewComponent } from './feessharing-new.component';

describe('FeessharingNewComponent', () => {
  let component: FeessharingNewComponent;
  let fixture: ComponentFixture<FeessharingNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FeessharingNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FeessharingNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

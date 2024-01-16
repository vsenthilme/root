import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OwnershipNewComponent } from './ownership-new.component';

describe('OwnershipNewComponent', () => {
  let component: OwnershipNewComponent;
  let fixture: ComponentFixture<OwnershipNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OwnershipNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(OwnershipNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StorageNewComponent } from './storage-new.component';

describe('StorageNewComponent', () => {
  let component: StorageNewComponent;
  let fixture: ComponentFixture<StorageNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StorageNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StorageNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

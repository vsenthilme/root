import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatesystemNewComponent } from './createsystem-new.component';

describe('CreatesystemNewComponent', () => {
  let component: CreatesystemNewComponent;
  let fixture: ComponentFixture<CreatesystemNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreatesystemNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreatesystemNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

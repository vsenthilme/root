import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrebillNewComponent } from './prebill-new.component';

describe('PrebillNewComponent', () => {
  let component: PrebillNewComponent;
  let fixture: ComponentFixture<PrebillNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrebillNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PrebillNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

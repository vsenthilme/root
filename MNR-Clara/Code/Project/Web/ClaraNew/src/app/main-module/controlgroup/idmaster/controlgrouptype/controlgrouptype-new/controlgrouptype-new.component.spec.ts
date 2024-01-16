import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ControlgrouptypeNewComponent } from './controlgrouptype-new.component';

describe('ControlgrouptypeNewComponent', () => {
  let component: ControlgrouptypeNewComponent;
  let fixture: ComponentFixture<ControlgrouptypeNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ControlgrouptypeNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ControlgrouptypeNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

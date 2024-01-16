import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GlmappingNewComponent } from './glmapping-new.component';

describe('GlmappingNewComponent', () => {
  let component: GlmappingNewComponent;
  let fixture: ComponentFixture<GlmappingNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GlmappingNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GlmappingNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartnerconfigrationNewComponent } from './partnerconfigration-new.component';

describe('PartnerconfigrationNewComponent', () => {
  let component: PartnerconfigrationNewComponent;
  let fixture: ComponentFixture<PartnerconfigrationNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PartnerconfigrationNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PartnerconfigrationNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SbuNewComponent } from './sbu-new.component';

describe('SbuNewComponent', () => {
  let component: SbuNewComponent;
  let fixture: ComponentFixture<SbuNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SbuNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SbuNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemnumberingNewComponent } from './systemnumbering-new.component';

describe('SystemnumberingNewComponent', () => {
  let component: SystemnumberingNewComponent;
  let fixture: ComponentFixture<SystemnumberingNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemnumberingNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemnumberingNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

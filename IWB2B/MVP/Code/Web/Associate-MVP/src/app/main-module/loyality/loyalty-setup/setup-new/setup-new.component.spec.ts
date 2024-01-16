import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SetupNewComponent } from './setup-new.component';

describe('SetupNewComponent', () => {
  let component: SetupNewComponent;
  let fixture: ComponentFixture<SetupNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SetupNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SetupNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCreationNewComponent } from './user-creation-new.component';

describe('UserCreationNewComponent', () => {
  let component: UserCreationNewComponent;
  let fixture: ComponentFixture<UserCreationNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserCreationNewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCreationNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

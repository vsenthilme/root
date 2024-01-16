import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserprofileCopyComponent } from './userprofile-copy.component';

describe('UserprofileCopyComponent', () => {
  let component: UserprofileCopyComponent;
  let fixture: ComponentFixture<UserprofileCopyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserprofileCopyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserprofileCopyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

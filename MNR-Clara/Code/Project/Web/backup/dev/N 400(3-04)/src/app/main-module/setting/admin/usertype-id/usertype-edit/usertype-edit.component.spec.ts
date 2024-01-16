import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsertypeEditComponent } from './usertype-edit.component';

describe('UsertypeEditComponent', () => {
  let component: UsertypeEditComponent;
  let fixture: ComponentFixture<UsertypeEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UsertypeEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UsertypeEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

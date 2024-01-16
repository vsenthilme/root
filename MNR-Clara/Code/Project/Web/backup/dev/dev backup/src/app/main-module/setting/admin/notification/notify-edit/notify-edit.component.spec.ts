import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotifyEditComponent } from './notify-edit.component';

describe('NotifyEditComponent', () => {
  let component: NotifyEditComponent;
  let fixture: ComponentFixture<NotifyEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotifyEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NotifyEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

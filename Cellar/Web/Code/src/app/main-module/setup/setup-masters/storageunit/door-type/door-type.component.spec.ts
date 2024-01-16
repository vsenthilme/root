import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DoorTypeComponent } from './door-type.component';

describe('DoorTypeComponent', () => {
  let component: DoorTypeComponent;
  let fixture: ComponentFixture<DoorTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DoorTypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DoorTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

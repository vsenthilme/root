import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PickupOpenComponent } from './pickup-open.component';

describe('PickupOpenComponent', () => {
  let component: PickupOpenComponent;
  let fixture: ComponentFixture<PickupOpenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PickupOpenComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PickupOpenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

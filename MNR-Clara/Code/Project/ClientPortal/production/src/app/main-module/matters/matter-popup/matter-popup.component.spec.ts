import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatterPopupComponent } from './matter-popup.component';

describe('MatterPopupComponent', () => {
  let component: MatterPopupComponent;
  let fixture: ComponentFixture<MatterPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatterPopupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatterPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

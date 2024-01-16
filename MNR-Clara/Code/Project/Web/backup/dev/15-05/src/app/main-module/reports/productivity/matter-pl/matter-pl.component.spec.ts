import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatterPLComponent } from './matter-pl.component';

describe('MatterPLComponent', () => {
  let component: MatterPLComponent;
  let fixture: ComponentFixture<MatterPLComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatterPLComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatterPLComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

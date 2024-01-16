import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatterIntakeComponent } from './matter-intake.component';

describe('MatterIntakeComponent', () => {
  let component: MatterIntakeComponent;
  let fixture: ComponentFixture<MatterIntakeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatterIntakeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatterIntakeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

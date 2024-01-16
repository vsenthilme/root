import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SendIntakeComponent } from './send-intake.component';

describe('SendIntakeComponent', () => {
  let component: SendIntakeComponent;
  let fixture: ComponentFixture<SendIntakeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SendIntakeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SendIntakeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

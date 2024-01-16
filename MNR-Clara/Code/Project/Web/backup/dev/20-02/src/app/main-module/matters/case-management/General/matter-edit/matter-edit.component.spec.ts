import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatterEditComponent } from './matter-edit.component';

describe('MatterEditComponent', () => {
  let component: MatterEditComponent;
  let fixture: ComponentFixture<MatterEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatterEditComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatterEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

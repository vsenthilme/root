import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatterActiviyComponent } from './matter-activiy.component';

describe('MatterActiviyComponent', () => {
  let component: MatterActiviyComponent;
  let fixture: ComponentFixture<MatterActiviyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatterActiviyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MatterActiviyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

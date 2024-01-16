import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ControlgrouptypeComponent } from './controlgrouptype.component';

describe('ControlgrouptypeComponent', () => {
  let component: ControlgrouptypeComponent;
  let fixture: ComponentFixture<ControlgrouptypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ControlgrouptypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ControlgrouptypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

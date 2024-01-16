import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ControlgroupComponent } from './controlgroup.component';

describe('ControlgroupComponent', () => {
  let component: ControlgroupComponent;
  let fixture: ComponentFixture<ControlgroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ControlgroupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ControlgroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

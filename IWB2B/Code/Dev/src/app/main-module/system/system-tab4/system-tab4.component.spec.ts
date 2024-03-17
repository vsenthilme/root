import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SystemTab4Component } from './system-tab4.component';

describe('SystemTab4Component', () => {
  let component: SystemTab4Component;
  let fixture: ComponentFixture<SystemTab4Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SystemTab4Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SystemTab4Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
